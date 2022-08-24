/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package repositories


import org.joda.time.{DateTime, DateTimeZone}
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Indexes.ascending
import org.mongodb.scala.model.{FindOneAndReplaceOptions, IndexModel, IndexOptions}
import play.api.Configuration
import play.api.libs.json.{Format, JsValue, Json, OFormat}
import repositories.DatedCacheMap.unapply
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.{Codecs, PlayMongoRepository}
import uk.gov.hmrc.mongo.play.json.formats.{MongoJavatimeFormats, MongoJodaFormats}

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DatedCacheMap(id: String,
                         data: Map[String, JsValue],
                         lastUpdated: DateTime = DateTime.now(DateTimeZone.UTC))

object DatedCacheMap {
  implicit val dateFormat: Format[DateTime] = MongoJodaFormats.dateTimeFormat //TODO: Temporary, future stories to switch to JavaTime
  implicit val formats: OFormat[DatedCacheMap] = Json.format[DatedCacheMap]

  def apply(cacheMap: CacheMap): DatedCacheMap = DatedCacheMap(cacheMap.id, cacheMap.data)
}

class ReactiveMongoRepository(config: Configuration, mongo: MongoComponent)
  extends PlayMongoRepository[DatedCacheMap](
    mongoComponent = mongo,
    collectionName = config.get[String]("appName"),
    domainFormat = DatedCacheMap.formats,
    indexes = Seq(IndexModel(
      ascending("lastUpdated"),
      IndexOptions()
        .name("userAnswersExpiry")
        .expireAfter(config.get[Int]("mongodb.timeToLiveInSeconds").toLong, TimeUnit.SECONDS)
    )),
    extraCodecs = Seq(Codecs.playFormatCodec(CacheMap.formats))
  ) {

  def upsert(cm: CacheMap): Future[Boolean] =
    collection.findOneAndReplace(
      filter = equal("id", cm.id),
      replacement = DatedCacheMap(cm),
      options = FindOneAndReplaceOptions().upsert(true)
    ).toFuture().map(_ => true)

  def get(id: String): Future[Option[CacheMap]] =
    collection.find[CacheMap](equal("id", id)).headOption()
}

@Singleton
class SessionRepository @Inject()(config: Configuration, reactiveMongoComponent: MongoComponent) {

  private lazy val sessionRepository: ReactiveMongoRepository = new ReactiveMongoRepository(config, reactiveMongoComponent)

  def apply(): ReactiveMongoRepository = sessionRepository
}

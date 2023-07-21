# 1.MongoDB?

## 1.1 소개
MongoDB는 C++로 작성된 오픈소스 문서지향(Document-Oriented) 적 Cross-platform 데이터베이스이며, 뛰어난 확장성과 성능을 자랑합니다 .[NoSQL](https://namu.wiki/w/NoSQL "NoSQL") [DBMS](https://namu.wiki/w/DBMS "DBMS")의 한 종류이며, [MySQL](https://namu.wiki/w/MySQL "MySQL") 처럼 전통적인 테이블-관계 기반의 [RDBMS](https://namu.wiki/w/RDBMS "RDBMS")가 아니라 [SQL](https://namu.wiki/w/SQL "SQL")을 사용하지 않는다. 또한, 현존하는 NoSQL 데이터베이스 중 인지도 1위를 유지하고 있습니다.

## 1.2 NoSQL?
흔히 NoSQL이라고 해서 아, SQL이 없는 데이터베이스구나! 라고 생각 할 수도 있겠지만, 진짜 의미는 Not Only SQL 입니다. 기존의 RDBMS의 한계를 극복하기 위해 만들어진 새로운 형태의 데이터저장소 입니다. 관계형 DB가 아니므로, RDMS처럼 고정된 스키마 및 JOIN 이 존재하지 않습니다.


## 1.3 특징
MongoDB는MySQL의 테이블과 같은 스키마가 고정된 구조 대신 [JSON](https://namu.wiki/w/JSON "JSON") 형태의 동적 스키마형 문서를 사용한다. 이를 MongoDB에서는 BSON(Binary JSON)이라는 독자적인 형식을 사용하는데, 데이터의 타입까지 포함되어 이진형태로 인코딩되어 저장되기 때문에 JSON과는 다른 차이점이 있다.  
  
MongoDB는 가장 기본적인 데이터를 Document 라고 부른다. 이는 MySQL같은 RDBMS에서는 row에 해당된다. 이 Document의 집합을 Collection이라고 하는데, RDBMS에서는 테이블(Table)에 해당된다. Collection의 집합은 DB이고, 이는 RDBMS에서도 동일하다. 

![ffff](https://velopert.com/wp-content/uploads/2016/02/ffff.png)
  
똑같은 조건으로 설계되었을 시 기존 RDBMS 속도보다 굉장히 빠르다는 장점이 있다. 이런 속도는 ACID를 포기한 댓가로 얻은 것이다. 따라서 데이터 일관성(Consistency)이 거의 필요 없고 조인 연산을 Embeded로 대체할 수 있는 경우에는 MongoDB가 확실한 대안이 될 수 있다. 반대로 저장하는 데이터가 은행 데이터 같이 일관성(Consistency)이 매우 중요한 작업에는 MongoDB를 쓰기 매우 힘들다.
```
ACID는 데이터베이스 트랜잭션의 속성을 설명하는 약어로 데이터베이스 트랜잭션이 안정성과 일관성을 보장하는지 확인하는 기준으로 사용됩니다
```


## 1.4 RDBMS와 비교 

|**RDBMS**|**MongoDB**||
|:--:|:--:|
|Database|Database|
|Table|Collection|
|Tuple / Row|Document|
|Column|Key / Field|
|Table Join|Embedded Documents|
|Primary Key|Primary Key (_id)|


#### MongoDB장점
- Schema-less (Schema가 없다. 같은 Collection 안에 있을지라도 다른 Schema를 가지고 있을 수 있다)
- 각 객체의 구조가 뚜렷하다
- 복잡한 JOIN 이 없다.
- Deep Query ability (문서지향적 Query Language 를 사용하여 SQL 만큼 강력한 Query 성능을 제공한다.
- 어플리케이션에서 사용되는 객체를 데이터베이스에 추가 할 때 Conversion / Mapping이 불필요하다.
#### MongoDB단점
- ‘기본설정’으로 데이터를 쓰는(Write)것은 안전하지 않음 (데이터 안전성의 문제)
- 써야 할 데이터 양이 많으면 감당하지 못함
- 데이터 삭제나 업데이트를 했을 때 단편화 문제를 발생시켜 데이터 처리에 필요 이상의 메모리를 사용
- 데이터 복제가 필요 이상의 서버를 차지한다는 이유  
- 몽고DB 속도는 인덱스 사이즈와 메모리에 달려 있는데, 메모리가 가득 차서 HDD로 내려가 데이터를 처리할 경우 처리 속도가 급감  
  

RDBMS는 고유의 정적인 특성으로 인해, 트랜잭션 데이터를 관리하는 경우와 같이 데이터 무결성 및 격리가 필수적인 상황에서는 MongoDB보다 선호될 수 있습니다. 그러나 MongoDB는 형식이 덜 제한적이고 성능이 우수하므로, 특별히 가용성과 속도를 최우선으로 고려하는 상황에서는 보다 나은 선택이 될 수있다.

* **MongoDB vs MySQL [비교자료](https://www.mongodb.com/ko-kr/compare/mongodb-mysql)** 

## 1.5 MongoDB 사용사례
1) 모바일 애플리케이션
* MongoDB의 JSON 문서 모델을 활용하면 [클라우드 기반 스토리지 솔루션](https://www.ibm.com/kr-ko/topics/cloud-storage)뿐만 아니라 Apple iOS 및 Android 디바이스를 비롯한 백엔드 애플리케이션 데이터를 필요한 곳 어디에나 저장할 수 있습니다.
2) 실시간 분석
* MongoDB는 JSON 및 JSON과 유사한 문서(예: BSON)를 아주 손쉽게 Java 객체로 변환하므로, 여러 개발 환경의 실시간 정보를 분석하는 경우 MongoDB를 사용하면 매우 효율적이고 빠르게 데이터를 읽고 쓸 수 있습니다.
3) 컨텐츠 관리 시스템
* MongoDB를 사용하면 고유의 고가용성과 단일 데이터베이스를 통해 신규 기능 및 속성을 온라인 애플리케이션과 웹사이트에 손쉽게 추가할 수 있습니다.
4) 엔터프라이즈 데이터 웨어하우스
* MongoDB 및 Hadoop(분산 파일 시스템)을 활용해 위험 모델링, 예측 분석, 실시간 데이터 처리를 수행할 수 있습니다.

## 1.6 Data Modelling
**Schema 디자인 할 때 고려사항**
- 사용자 요구 (User Requirement) 에 따라 schema를 디자인한다.
- 객체들을 함께 사용하게 된다면 한 Document에 합쳐서 사용한다. (예: 게시물-댓글 과의 관계)  
    그렇지 않으면 따로 사용한다 (그리고 JOIN 을 사용하지 않는 걸 확실히 해둔다)
- 읽을 때 JOIN 하는게 아니라 데이터를 작성 할 때 JOIN 한다.

**같은 데이터베이스 디자인에 대한 RDBMS와 MongoDB의 차이**
예)
* 게시글에는 작성자 이름, 제목, 내용이 담겨져 있다.  
* 각 게시글은 0개 이상의 태그를 가지고 있을 수 있다.  
* 게시글엔 댓글을 달 수 있다. 댓글은 작성자 이름, 내용, 작성 시간을 담고 있다


* RDBMS 디자인의 경우 (테이블 3개를 생성하여 JOIN)
![d](https://velopert.com/wp-content/uploads/2016/02/d.png)

* MongoDB 디자인 (NoSQL 에선 하나의 Document로 처리)
```
{
 _id: POST_ID,
 title: POST_TITLE,
 content: POST_CONTENT,
 username: POST_WRITER,
 tags: [ TAG1, TAG2, TAG3 ],
 time: POST_TIME
 comments: [
 { 
 username: COMMENT_WRITER,
 mesage: COMMENT_MESSAGE,
 time: COMMENT_TIME
 },
 { 
 username: COMMENT_WRITER,
 mesage: COMMENT_MESSAGE,
 time: COMMENT_TIME
 }
 ]
}
```


####  Reference
* https://namu.wiki/w/MongoDB
* https://www.ibm.com/kr-ko/topics/mongodb
* https://velopert.com/436
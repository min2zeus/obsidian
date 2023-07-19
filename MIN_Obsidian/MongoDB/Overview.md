# 1.MongoDB?
## 1.1 소개
MongoDB는 C++로 작성된 오픈소스 문서지향(Document-Oriented) 적 Cross-platform 데이터베이스이며, 뛰어난 확장성과 성능을 자랑합니다 .[NoSQL](https://namu.wiki/w/NoSQL "NoSQL") [DBMS](https://namu.wiki/w/DBMS "DBMS")의 한 종류이며, [MySQL](https://namu.wiki/w/MySQL "MySQL") 처럼 전통적인 테이블-관계 기반의 [RDBMS](https://namu.wiki/w/RDBMS "RDBMS")가 아니라 [SQL](https://namu.wiki/w/SQL "SQL")을 사용하지 않는다. 또한, 현존하는 NoSQL 데이터베이스 중 인지도 1위를 유지하고 있습니다.

## 1.2 NoSQL?
흔히 NoSQL이라고 해서 아, SQL이 없는 데이터베이스구나! 라고 생각 할 수도 있겠지만, 진짜 의미는 Not Only SQL 입니다. 기존의 RDBMS의 한계를 극복하기 위해 만들어진 새로운 형태의 데이터저장소 입니다. 관계형 DB가 아니므로, RDMS처럼 고정된 스키마 및 JOIN 이 존재하지 않습니다.


## 1.3 특징
MongoDB는MySQL의 테이블과 같은 스키마가 고정된 구조 대신 [JSON](https://namu.wiki/w/JSON "JSON") 형태의 동적 스키마형 문서를 사용한다. 이를 MongoDB에서는 BSON(Binary JSON)이라는 독자적인 형식을 사용하는데, 데이터의 타입까지 포함되어 이진형태로 인코딩되어 저장되기 때문에 JSON과는 다른 차이점이 있다.  
  
MongoDB는 가장 기본적인 데이터를 Document 라고 부른다. 이는 MySQL같은 RDBMS에서는 row에 해당된다. 이 Document의 집합을 Collection이라고 하는데, RDBMS에서는 테이블(Table)에 해당된다. Collection의 집합은 DB이고, 이는 RDBMS에서도 동일하다.  
  
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
관련 예제


### 1.3.1 REST 특징 / 기준
1) Uniform (균일한 인터페이스)
	- Uniform Interface는 URI로 지정한 리소스에 대한 조작을 통일되고 한정적인 인터페이스로 수행하는 아키텍처 스타일을 말합니다.(표준 형식)
2) Stateless (무상태성) [(stateless)](https://www.redhat.com/ko/topics/cloud-native-apps/stateful-vs-stateless) 
	* REST는 무상태성 성격을 갖습니다. 다시 말해 작업을 위한 상태 정보를 따로 저장하고 관리하지 않습니다. 세션 정보나 쿠키정보를 별도로 저장하고 관리하지 않기 때문에 API 서버는 들어오는 요청만을 단순히 처리하면 됩니다. 때문에 서비스의 자유도가 높아지고 서버에서 불필요한 정보를 관리하지 않음으로써 구현이 단순해집니다.
3) Cacheable (캐시 가능)
	* 서버 응답 시간을 개선하기 위해 클라이언트 또는 중개자에 일부 응답을 저장하는 프로세스인 캐싱을 지원합니다. REST의 가장 큰 특징 중 하나는 HTTP라는 기존 웹표준을 그대로 사용하기 때문에, 웹에서 사용하는 기존 인프라를 그대로 활용이 가능합니다. 따라서 HTTP가 가진 캐싱 기능이 적용 가능합니다. HTTP 프로토콜 표준에서 사용하는 Last-Modified태그나 E-Tag를 이용하면 캐싱 구현이 가능합니다.
4) Self-descriptiveness (자체 표현 구조)
	* REST의 또 다른 큰 특징 중 하나는 REST API 메시지만 보고도 이를 쉽게 이해 할 수 있는 자체 표현 구조로 되어 있다는 것입니다. 
5) Client - Server 구조
	*  REST 서버는 API 제공, 클라이언트는 사용자 인증이나 컨텍스트(세션, 로그인 정보)등을 직접 관리하는 구조로 각각의 역할이 확실히 구분되기 때문에 클라이언트와 서버에서 개발해야 할 내용이 명확해지고 서로간 의존성이 줄어들게 됩니다.
6) 계층형 구조
	*  REST 서버는 다중 계층으로 구성될 수 있으며 보안, 로드 밸런싱, 암호화 계층을 추가해 구조상의 유연성을 둘 수 있고 PROXY, 게이트웨이 같은 네트워크 기반의 중간매체를 사용할 수 있게 합니다. 이러한 계층은 클라이언트에 보이지 않는 상태로 유지됩니다.
7) 코드 온디맨드 (선택사항)
	* 요청을 받으면 서버에서 클라이언트로 실행 가능한 코드를 전송하여 클라이언트 기능을 확장할 수 있는 기능.
#### <span style='color:#edf0f5'>-- dump data</span>
* API가 RESTful로 간주되기 위한 기준
	- 클라이언트, 서버 및 리소스로 구성되었으며 요청이 HTTP를 통해 관리되는 클라이언트-서버 아키텍처
	- [스테이트리스(stateless)](https://www.redhat.com/ko/topics/cloud-native-apps/stateful-vs-stateless) 클라이언트-서버 커뮤니케이션: GET 요청 간에 클라이언트 정보가 저장되지 않으며, 각 요청이 분리되어 있고 서로 연결되어 있지 않음
	- 클라이언트-서버 상호 작용을 간소화하는 캐시 가능 데이터
	- 정보가 표준 형식으로 전송되도록 하기 위한 구성 요소 간 통합 인터페이스. 여기에 필요한 것은 다음과 같습니다.
	    - 요청된 리소스가 식별 가능하며 클라이언트에 전송된 표현과 분리되어야 합니다.
	    - 수신한 표현을 통해 클라이언트가 리소스를 조작할 수 있어야 합니다(이렇게 할 수 있는 충분한 정보가 표현에 포함되어 있기 때문).
	    - 클라이언트에 반환되는 자기 기술적(self-descriptive) 메시지에 클라이언트가 정보를 어떻게 처리해야 할지 설명하는 정보가 충분히 포함되어야 합니다.
	    - 하이퍼텍스트/하이퍼미디어를 사용할 수 있어야 합니다. 즉, 클라이언트가 리소스에 액세스한 후 하이퍼링크를 사용해 현재 수행 가능한 기타 모든 작업을 찾을 수 있어야 합니다.
	- 요청된 정보를 검색하는 데 관련된 서버(보안, 로드 밸런싱 등을 담당)의 각 유형을 클라이언트가 볼 수 없는 계층 구조로 체계화하는 계층화된 시스템.
	- 코드 온디맨드(선택 사항): 요청을 받으면 서버에서 클라이언트로 실행 가능한 코드를 전송하여 클라이언트 기능을 확장할 수 있는 기능
* 
### 1.3.2 REST 이점
1) 확장성
	- REST API를 구현하는 시스템은 REST가 클라이언트-서버 상호 작용을 최적화하기 때문에 효율적으로 크기 조정할 수 있습니다. 무상태 / 캐싱 같은 기능으로 성능을 저하시키는 통신 병목 현상을 일으키지 않으면서 확장성을 지원합니다.
2) 유연성
	* RESTful 웹 서비스는 완전한 클라이언트-서버 분리를 지원합니다. 각 부분이 독립적으로 발전할 수 있도록 다양한 서버 구성 요소를 단순화하고 분리합니다. 서버 애플리케이션의 플랫폼 또는 기술 변경은 클라이언트 애플리케이션에 영향을 주지 않습니다. 애플리케이션 함수를 계층화하는 기능은 유연성을 더욱 향상시킵니다. 예를 들어, 개발자는 애플리케이션 로직을 다시 작성하지 않고도 데이터베이스 계층을 변경할 수 있습니다.
3) 독립성
	* REST API는 사용되는 기술과 독립적입니다. API 설계에 영향을 주지 않고 다양한 프로그래밍 언어로 클라이언트 및 서버 애플리케이션을 모두 작성할 수 있습니다. 또한 통신에 영향을 주지 않고 양쪽의 기본 기술을 변경할 수 있습니다.

### 1.3.3 REST vs SOAP
 REST API는 따라야 할 기준이 있지만, 특정 요구사항이 있어 속도를 저하시키고 무겁게 만드는 SOAP(Simple Object Access Protocol) 등의 규정된 프로토콜보다 사용하기 쉬운 것으로 간주된다.
 
* REST와 SOAP는 각기 다른 두 가지의 온라인 데이터 전송 방식입니다.
* SOAP는 프로토콜이지만, REST는 프로토콜이 아니라는 점이 주요 차이점
* 일반적으로 API는 활용 사례와 개발자의 선호에 따라 REST 또는 SOAP 중 하나를 준수

대부분의 레거시 시스템에서 SOAP를 준수하며, REST는 그보다 뒤에 고려하거나 웹 기반 시나리오에서의 더 빠른 대안으로 여기는 경우가 많습니다. REST는 유연한 구현을 제공하는 가이드라인 세트고, SOAP는 XML 메시징과 같은 특정 요건이 있는 프로토콜입니다.

REST API는 경량화되어 있기 때문에 [사물 인터넷(IoT)](https://www.redhat.com/ko/topics/internet-of-things-570051), 모바일 애플리케이션 개발, [서버리스(servreless) 컴퓨팅](https://www.redhat.com/ko/topics/cloud-native-apps/what-is-serverless)과 같이 보다 새로운 컨텍스트에 이상적입니다. SOAP 웹 서비스는 많은 기업에서 필요로 하는 기본 보안과 트랜잭션 컴플라이언스를 제공하지만, 이로 인해 좀 더 무거운 경향이 있습니다. 또한 Google Maps API와 같은 대부분의 퍼블릭 API는 REST 가이드라인을 따릅니다.
### 1.3.4 REST 구성요소
* 자원(RESOURCE) - URI
- 행위(Verb) - HTTP METHOD
- 표현(Representations)
### 1.3.5 REST API 디자인 가이드
REST API 설계 시 가장 중요한 항목은 다음의 2가지로 요약할 수 있다.
* URI는 정보의 자원을 표현해야 한다.  
* 자원에 대한 행위는 HTTP Method(GET, POST, PUT, DELETE)로 표현한다
#### 1.3.5.1 REST API 중심 규칙
1) URI는 정보의 자원을 표현해야 한다. (리소스명은 동사보다는 명사를 사용)
```
GET /members/delete/1
```
* 위는 잘못된 표현 예시
* URI는 자원을 표현하는데 중점
* delete와 같은 행위에 대한 표현이 들어가서는 안됨

2) 자원에 대한 행위는 HTTP Method(GET, POST, PUT, DELETE 등)로 표현
```
DELETE /members/1
```
3) 회원정보를 가져오는 URI
```
    GET /members/show/1     (x)
    GET /members/1          (o)
```
4) 회원을 추가할 때
```
    GET /members/insert/2 (x)  - GET 메서드는 리소스 생성에 맞지 않습니다.
    POST /members/2       (o)
```

5) HTTP METHOD의 알맞은 역할
URI는 자원을 표현하는 데에 집중하고 행위에 대한 정의는 HTTP METHOD를 통해 하는 것이 REST한 API를 설계하는 중심 규칙입니다.

|METHOD|역할||
|:--:|:--|
|POST|POST를 통해 해당 URI를 요청하면 리소스를 생성합니다.|
|GET|GET를 통해 해당 리소스를 조회합니다. 리소스를 조회하고 해당 도큐먼트에 대한 자세한 정보를 가져온다.|
|PUT|PUT를 통해 해당 리소스를 수정합니다.|
|DELETE|DELETE를 통해 리소스를 삭제합니다.|


#### 1.3.5.2 URI 설계 시 유의사항
1) 슬래시 구분자(/)는 계층 관계를 나타내는 데 사용
```
    http://restapi.example.com/houses/apartments
    http://restapi.example.com/animals/mammals/whales
```
3) URI 마지막 문자로 슬래시(/)를 포함하지 않는다.
```
    http://restapi.example.com/houses/apartments/ (X)
    http://restapi.example.com/houses/apartments  (0)
```
5) 하이픈(-)은 URI 가독성을 높이는데 사용
6) 밑줄 _ 은 URI에 사용하지 않는다.
7) URI 경로에는 소문자가 적합하다.
	* URI 경로에 대문자 사용은 피하도록 해야 합니다. 대소문자에 따라 다른 리소스로 인식하게 되기 때문
8) 파일 확장자는 URI에 포함시키지 않는다.
```
 http://restapi.example.com/members/soccer/345/photo.jpg (X)
```
	* REST API에서는 메시지 바디 내용의 포맷을 나타내기 위한 파일 확장자를 URI 안에 포함시키지 않습니다. Accept header를 사용하도록 합시다.
```
GET / members/soccer/345/photo HTTP/1.1 Host: restapi.example.com Accept: image/jpg
```
#### 1.3.5.3 리소스 간의 관계를 표현하는 방법
REST 리소스 간에는 연관 관계가 있을 수 있고, 이런 경우 다음과 같은 표현방법으로 사용합니다.
```
             /리소스명/리소스 ID/관계가 있는 다른 리소스명

ex)    GET : /users/{userid}/devices (일반적으로 소유 ‘has’의 관계를 표현할 때)
```
만약에 관계명이 복잡하다면 이를 서브 리소스에 명시적으로 표현하는 방법이 있습니다. 예를 들어 사용자가 ‘좋아하는’ 디바이스 목록을 표현해야 할 경우 다음과 같은 형태로 사용될 수 있습니다.
```
    GET : /users/{userid}/likes/devices (관계명이 애매하거나 구체적 표현이 필요할 때)
```

#### 1.3.5.4 자원을 표현하는 Colllection과 Document
Collection과 Document에 대해 알면 URI 설계가 한 층 더 쉬워집니다. DOCUMENT는 단순히 문서로 이해해도 되고, 한 객체라고 이해하셔도 될 것 같습니다. 컬렉션은 문서들의 집합, 객체들의 집합이라고 생각하시면 이해하시는데 좀더 편하실 것 같습니다. 컬렉션과 도큐먼트는 모두 리소스라고 표현할 수 있으며 URI에 표현됩니다.
```
	sports라는 컬렉션과 soccer라는 도큐먼트로 표현
	http:// restapi.example.com/sports/soccer
```
```
	sports, players 컬렉션과 soccer, 13(13번인 선수)를 의미하는 도큐먼트
	http:// restapi.example.com/sports/soccer/players/13

여기서 중요한 점은 컬렉션은 복수로 사용하고 있다는 점입니다. 좀 더 직관적인 REST API를 위해서는 컬렉션과 도큐먼트를 사용할 때 단수 복수도 지켜준다면 좀 더 이해하기 쉬운 URI를 설계할 수 있습니다.
```
#### 1.3.5.5 HTTP 응답 상태 코드
잘 설계된 REST API는 URI만 잘 설계된 것이 아닌 그 리소스에 대한 응답을 잘 내어주는 것까지 포함되어야 합니다. 정확한 응답의 상태코드만으로도 많은 정보를 전달할 수가 있기 때문에 응답의 상태코드 값을 명확히 돌려주는 것은 생각보다 중요한 일이 될 수도 있습니다.
![[http_상태코드.png]]


####  Reference
* https://meetup.nhncloud.com/posts/92
* https://www.redhat.com/ko/topics/api/what-is-a-rest-api
* https://aws.amazon.com/ko/what-is/restful-api/


---
title:RESTFUL API
tag : RESTFUL
---

## ==REST API==
**- 핵심 Contents 및 기능을 외부 사이트에서 활용할 수 있도록 제공되는 아키텍쳐**
	→ 웹의 장점을 최대한으로 사용하기 위해서 고안
	ex) 네이버 블로그에서 글을 저장하고, 글 목록을 읽어가도록 외부에 기능을 제공하거나, 구글에서 구글 지도를 사용하도록 제공 등
	
- SPA(Single Page Application) 방식으로 개발된 프론트엔드에서 백엔드의 데이터를 가져올 때 가장 많이 사용되는 자원(Resource) 처리 방식이다.
- HTTP Method를 이용해 각각의 자원에 CRUD 작업을 한다.

**→ "Represent"가 "표현하다"는 뜻인 것처럼 요청하는 내용을 보고 무엇을 요청하는 것인지 <u>직관적으로</u> 이해할 수 있다.**


## REST 구성
**- 자원(Resource)** : REST의 가장 중요한 요소 중 하나로 말 그대로 시스템의 자원. (HTTP의 URI로 표현)
	예)  영화 예매 시스템의 사용자, 예약번호, 좌석번호, 영화정보 등이 시스템의 resource
	
**- 행위(Verb)** : Resource의 행위를 정의. (HTTP Method로 표현)
	→ GET(조회)/POST(생성)/PUT(갱신)/DELETE(삭제)를 통해서 CRUD를 실행
	
**- 표현(Representations)** : Resource 행위에 대한 내용을 정의 (HTTP Message Pay Load로 표현)

##### " 즉, REST API는 URI로 자원(Resource)을 요청하여 특정 형태로 표현(Representation)한다는 것과 HTTP Method를 적극적으로 활용하여 행위(Verb)를 나타낸다"



## REST의 특징

**1) Client-Server(클라이언트와 서버 구조)**
: REST 서버는 API를 제공, 클라이언트는 session, 정보들을 직접 관리하는 구조로 서버와 클라이언트의 개발 영역이 명확하게 나뉘고, 서로 의존성이 떨어지게 된다.
→ Front-End / Back-End의 역할이 분명해짐.

**2) Stateless(무상태성)**
: REST는 상태 정보를 따로 저장 및 관리하지 않는다(Stateless). 세션이나 쿠키 정보를 별도로 저장, 관리하지 않으므로 API 서버는 들어오는 request만 단순 처리하면 된다. 따라서, 서비스 자유도가 높아지고 서버에 불필요한 정보를 관리하지 않으므로 구현이 간결해진다.

**3) Cache(캐시 가능)**
: REST의 가장 큰 특징은 HTTP라는 기존의 웹 표준을 그대로 따른다는 것이다. 즉, 기존의 웹 자원들을 그대로 활용할 수 있다는 점이다. 따라서 HTTP가 가진 캐싱(임시 저장)기능이 적용 가능하다.

**4)  Uniform Interface (유니폼 인터페이스)**
: Uniform Interface는 URI로 지정한 리소스에 대한 조작을 통일되고 한정적인 인터페이스로 수행하는 아키텍처 스타일.

**5) Layered System**
:REST 서버는 다중 계층으로 구성될 수 있다.

**6) Self-descriptiveness(자체 표현 구조)**
: REST는 또한 REST API 내용만 보고도 별도 문서 없이 쉽게 이해할 수 있다.


➡️위의 조건들을 만족해야 REST!
: HTTP 프로토콜을 이용하면 위의 조건을 쉽게 구현할 수 있는데, 한 가지 조건이 걸린다.
바로 <u>Uniform Interface</u> 이다.

- Uniform Interface 스타일 특징
	1. 리소스가 URI로 식별되어야 한다.
	2. 리소스를 생성/수정/추가하고자 할 때 HTTP메시지에 표현해서 전송해야 한다.
	3. 메시지는 스스로 설명할 수 있어야 한다.(Self-descriptive message)
	4. 애플리케이션의 상태는 하이퍼링크를 통해서 전이되어야 한다.([HATEOAS](###HATEOAS))

 1,2번 항목은 지키기 어렵지 않으나, 3,4번 항목은 web과는 다르게 API로 쉽지 않다.
요청에 대한 응답 결과로 보통 **JSON 포맷**을 많이 사용하곤 한다. 그런데 이 JSON 메시지가 어디에 전달되는지 그리고 JSON 메시지를 구성하는 것이 어떤 의미를 표현해야 메시지 스스로 설명할 수 있다고 할 수 있는데 쉽지 않다.

**=> REST의 모든 것을 제공하지 않기 때문에 Web API 혹은 HTTP API라 불러야 한다.**


##### HATEOAS
(Hypermedia as the Engine of Application State)
: 어떤 자원에 대해 처리를 했을 때, 그에 대한 응답으로 프론트엔드에서 처리가 용이하도록 자원에 대한 Links 를 함께 제공.


## RESTful?
: REST API는 기본적으로 URI로 자원을 표현하고, 자원에 대한 행위는 HTTP Method를 사용한다는 점이다. 다른 부분보다 가장 우선 시 해야 하고, 일부는 선택적으로 제공한다.
실제로 REST API를 제공하는 이름이 알려진 서비스들도 규칙을 완전히 준수하는 것은 아니며 기능을 제공하지 않는 경우도 많다.

 **< REST의 원리를 따르는 시스템 > 
REST를 REST답게 쓰기 위한 방법으로 공식적이지 않은 개발자들이 비공식적으로 제시한 것.
이해하기 쉽고 사용하기 쉬운 REST API를 만드는 것이 목적이다**

- CRUD의 기능을 전부 활용해야 함.
	ex) post로만 모든 것을 처리하면 RESTful이 아니다.
	
- route에 resource, id 외의 정보가 들어가면 안된다.


**- URI 규칙 확인**
![](https://i.imgur.com/5z9eQqB.png)

**- HTTP 메서드 적극적으로 사용**
: REST API는 URI에 동사를 직접 명시하는 대신 HTTP Method로 무엇을 할지 명시한다.
가장 많이 사용되는 메서드는 GET, POST, PUT, DELETE이며 PUT 대신 PATCH를 사용하는 경우도 있고 완성도 높은 API 제공을 위해 추가적으로 OPTIONS, HEAD를 제공하기도 한다.

![](https://i.imgur.com/f2F004v.png)

	- PATCH : 일부 자원을 수정하기에 적합. PUT은 모든자원.
	- OPTIONS : 자원에 대해 사용 가능한 메서드를 반환.
	- HEAD : Body를 제외한 Header만 반환.

➡️ ==URI를 사용하여 자원을 나타내고 XML, JSON 등의 포맷을 통해 표현하고 행위는 HTTP 메서드로 나타내었는가?==



## RESTful API 장점
**1) 보기 좋다.**
: URL만 보고 어떤 자원에 접근할 것인지, 메소드를 보고 어떤 행위를 할지 알 수 있기 때문에 개발할 때 용이하다.

**2) 자원 절약**
: 1개의 URI로 4개의 행위(CRUD)를 명시할 수 있기 때문에 굉장히 효율적이다.

**3) stateless한 상태를 유지할 수 있다.**
: REST API의 가장 큰 특징으로 다양한 브라우저와 모바일에서 통신할 수 있도록 한다.
 클라이언트가 서버에 종속적이지 않아도 되기 때문에, scale out한 상황에서도 용이하다.





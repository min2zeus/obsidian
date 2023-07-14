# 1.RESTful API란?
## 1.1 개념 / 탄생
REST(RESTful, Representational State Transfer, RESTful, 레스트풀) API는<span style='color:#20bf6b'> REST 아키텍처 스타일의 제약 조건을 준수하고 RESTful 웹 서비스와 상호 작용할 수 있도록 하는 애플리케이션 프로그래밍 인터페이스</span>(API 또는 웹 API)입니다. REST는 Representational State Transfer의 줄임말이며, 2000년도에 로이 필딩 (Roy Fielding)의 박사학위 논문에서 최초로 소개되었습니다. 로이 필딩은 HTTP의 주요 저자 중 한 사람으로 그 당시 웹(HTTP) 설계의 우수성에 비해 제대로 사용되어지지 못하는 모습에 안타까워하며 웹의 장점을 최대한 활용할 수 있는 아키텍처로써 REST를 발표했다고 합니다.

## 1.2 API란 ?
* API는 애플리케이션 소프트웨어를 구축하고 통합하기 위한 정의 및 프로토콜 세트
* API는 정보 제공자와 정보 사용자 간의 계약으로 지칭되며 소비자에게 필요한 콘텐츠(호출)와 생산자에게 필요한 콘텐츠(응답)를 구성
	- ex) 날씨 서비스용 API 설계에서는 사용자는 우편번호를 제공하고, 생산자는 두 부분(첫 번째는 최고 기온, 두 번째는 최저 기온)으로 구성된 응답으로 답하도록 지정할 수 있습니다.  
* **컴퓨터나 시스템과 상호 작용하여 정보를 검색하거나 기능을 수행하고자 할 때 API는 사용자가 원하는 것을 시스템에 전달할 수 있게 지원하여 시스템이 이 요청을 이해하고 이행하도록 하는 것**
* API를 사용자 또는 클라이언트, 그리고 사용자와 클라이언트가 얻으려 하는 리소스 또는 웹 서비스 사이의 조정자로 생각할 수 있음
	* 웹 API는 클라이언트와 웹 리소스 사이의 게이트웨이
* API는 조직이 보안, 제어, 인증을 유지 관리(누가 무엇에 액세스할 수 있는지 결정)하면서 리소스와 정보를 공유할 수 있는 방법
* API의 또 다른 장점은 캐싱, 즉 리소스 검색 방법 또는 리소스의 출처에 대해 자세히 알 필요가 없다는 것이다.

## 1.3 REST
- REST는 프로토콜이나 표준이 아닌 아키텍처 제약 조건입니다. API 개발자는 REST를 다양한 방식으로 구현할 수 있습니다.
- RESTful API를 통해 클라이언트 요청이 수행될 때 RESTful API는 리소스 상태에 대한 표현을 요청자 또는 엔드포인트에 전송합니다. 이 정보 또는 표현은 HTTP: JSON(Javascript Object Notation), HTML, XLT, Python, PHP 또는 일반 텍스트를 통해 몇 가지 형식으로 전송됩니다. JSON은 그 이름에도 불구하고 사용 언어와 상관이 없을 뿐 아니라 인간과 머신이 모두 읽을 수 있기 때문에 가장 널리 사용되는 파일 형식입니다. 
- 그 외에 헤더와 매개 변수는 요청의 메타데이터, 권한 부여, URI(Uniform Resource Identifier), 캐싱, 쿠키 등에 대한 중요한 식별자 정보를 포함하고 있기 때문에 RESTful API HTTP 요청의 HTTP 메서드에서도 중요하다는 점을 유의해야 합니다. 요청 헤더와 응답 헤더가 있으며, 각각 고유한 HTTP 연결 정보 및 상태 코드가 있습니다.

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
	- REST API를 구현하는 시스템은 REST가 클라이언트-서버 상호 작용을 최적화하기 때문에 효율적으로 크기 조정할 수 있습니다. 무상태 캐싱 같은 기능으로 성능을 저하시키는 통신 병목 현상을 일으키지 않으면서 확장성을 지원합니다.
2) 유연성
	* RESTful 웹 서비스는 완전한 클라이언트-서버 분리를 지원합니다. 각 부분이 독립적으로 발전할 수 있도록 다양한 서버 구성 요소를 단순화하고 분리합니다. 서버 애플리케이션의 플랫폼 또는 기술 변경은 클라이언트 애플리케이션에 영향을 주지 않습니다. 애플리케이션 함수를 계층화하는 기능은 유연성을 더욱 향상시킵니다. 예를 들어, 개발자는 애플리케이션 로직을 다시 작성하지 않고도 데이터베이스 계층을 변경할 수 있습니다.
3) 독립성
	* REST API는 사용되는 기술과 독립적입니다. API 설계에 영향을 주지 않고 다양한 프로그래밍 언어로 클라이언트 및 서버 애플리케이션을 모두 작성할 수 있습니다. 또한 통신에 영향을 주지 않고 양쪽의 기본 기술을 변경할 수 있습니다.
	* 
* 이처럼 REST API는 따라야 할 기준이 있지만, 속도를 저하시키고 더 무겁게 만드는 XML 메시징, 빌트인 보안 및 트랜잭션 컴플라이언스처럼 특정 요구 사항이 있는 SOAP(Simple Object Access Protocol) 등의 규정된 프로토콜보다 사용하기 쉬운 것으로 간주
* 이와 대조적으로 REST는 필요에 따라 구현할 수 있는 일련의 지침으로, 이를 통해 REST API는 더 빨라지고 경량화되며 확장성이 증대되어 [사물인터넷(IoT)](https://www.redhat.com/ko/topics/internet-of-things-570051) 및 [모바일 애플리케이션 개발](https://www.redhat.com/ko/topics/mobile)에 가장 적합한 API
### 1.3.2 REST 구성요소
* 자원(RESOURCE) - URI
- 행위(Verb) - HTTP METHOD
- 표현(Representations)
- 





	* Git은 기본적으로 로컬 저장소만 필요.
	* 원격 저장소 서비스는 gitlab, github에서 제공.
	* gitlab은 일반적으로 private 저장소로 이용. (private project)
	* github는 일반적으로 public 저장소로 이용. (open source project)
	![[Pasted image 20230711135444.png]]

## 1.4 Git 설치
* Ubuntu (10.10.1.121)
```
apt install git-all 
 (root 계정)
```

```
  git --version
 	(git --version 으로 버전 조회 가능.)
```

# 2. GIT 기본

### 2.1 로컬 리포지토리 초기화
```
cd /home/hong/git/no1
git init

git config --global user.name "JM"
git config --global user.email "chongminy@naver.com"

git commit -m 'initial project version'
```

### 2.2기존 리포지토리 복제
```
git clone <URL>

example1)
git clone https://github.com/libgit2/libgit2

example2)
git clone https://github.com/libgit2/libgit2 mylibgit
```

### 2.3 파일 상태 확인
```
git status
git status -s
```

### 2.4 새 파일 추가
```
git add filename
```

### 2.5 파일 무시
```
vi .gitignore

# ignore all .a files
*.a

# but do track lib.a, even though you're ignoring .a files above
!lib.a

# only ignore the TODO file in the current directory, not subdir/TODO
/TODO

# ignore all files in any directory named build
build/

# ignore doc/notes.txt, but not doc/server/arch.txt
doc/*.txt

# ignore all .pdf files in the doc/ directory and any of its subdirectories
doc/**/*.pdf
```

### 2.6 단계적 변경 보기
```
git diff
git diff --staged
```
변경되었지만, 아직 준비되지 않은 항목 조회 가능.

### 2.7 변경 사항 커밋
```
git commit -m '20230711 14:24 version'
```

### 2.8 파일 제거
```
git rm filename
```

### 2.9 파일 이동
```
git mv filename
```

### 2.10 커밋 기록 보기
```
git log

git log --patch
(패치 출력)

git log --stat
(각 커밋에 대한 축약 통계 조회)

git log --pretty=oneline
(로그 출력을 기본값 이외의 형식으로 변경 oneline 외에 short, full, fuller 존재)

기타 옵션
-p 각 커밋과 함께 도입된 패치를 표시합니다.
--stat 각 커밋에서 수정된 파일에 대한 통계를 표시합니다.
--shortstat 명령에서 변경/삽입/삭제 줄만 표시합니다 --stat.
--name-only 커밋 정보 이후 수정된 파일 목록을 보여줍니다.
--name-status 정보가 추가/수정/삭제된 파일 목록도 함께 보여줍니다.
--abbrev-commit 40개 모두가 아닌 SHA-1 체크섬의 처음 몇 문자만 표시합니다.
--relative-date 전체 날짜 형식을 사용하는 대신 상대 형식(예: "2주 전")으로 날짜를 표시합니다.
--graph 로그 출력 옆에 분기 및 병합 이력의 ASCII 그래프를 표시합니다.
--pretty 대체 형식으로 커밋을 표시합니다. 옵션 값에는 oneline, short, full, fuller및 format(여기서 사용자 고유의 형식 지정)가 포함됩니다.
--oneline 함께 사용하는 줄임말 --pretty=oneline --abbrev-commit.
```

### 2.11 로그 출력 제한
```console
git log --since=2.weeks
(2주 동안의 커밋 목록 출력)

git log -S function_name
(문자열의 발생 횟수를 변경한 커밋만 표시)

기타 옵션
-<n> 마지막 n 커밋만 표시합니다.
--since,--after 커밋을 지정된 날짜 이후에 이루어진 커밋으로 제한합니다.
--until,--before 커밋을 지정된 날짜 이전에 이루어진 커밋으로 제한합니다.
--author 작성자 항목이 지정된 문자열과 일치하는 커밋만 표시합니다.
--committer 커미터 항목이 지정된 문자열과 일치하는 커밋만 표시합니다.
--grep 문자열이 포함된 커밋 메시지가 있는 커밋만 표시합니다.
-S 문자열과 일치하는 코드를 추가하거나 제거하는 커밋만 표시합니다.
```

### 2.12 변경 사항 되돌리기
```
rm test.txt
git checkout -- test.txt
(삭제, 변경된 파일을 checkout하면 현재 작업 중인 브랜치의 최신 커밋을 참조하여 파일을 되돌림)
```

### 2.13 reset으로 staging 취소하기
```
git reset --hard HEAD test.txt
(--hard 옵션을 추가하면 현재 상태가 HEAD와 같아짐. staging 해제 후 삭제한 파일 복구)

결국 아래와 동일한 효과
git reset HEAD test.txt
get checkout -- test.txt

```

### 2.14 commit 수정하기
```
git commit --amend
(commit에 추가할 파일을 놓친 경우 파일들을 staging 시켜주고 위 명령 수)
```

### 2.15 reset으로 commit 취소하기
```
(reset 명령은 HEAD가 가리키는 commit을 변경해줌)

git log --oneline

hong@1-121-TEST2-UBU18-HJM:~/git/no1$ git log --oneline
11fe152 (HEAD -> master) 20230711 19:33 version
4c4a463 20230711 14:24 version

git reset 4c4a463

```

### 2.16 원격 저장소 추가
```
git remote add test_pb <URL>

git remote
(추가한 원격 저장소 조회)

git remote -v
(추가한 원격 저장소 자세한 정보 조회)

git remote show test_pb
```

### 2.17 원격 저장소에서 가져오기
```
git fetch <remote>
```

### 2.18 원격 저장소에 push
```
git push test_pb master
```

### 2.19 git Alias
```
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status
git config --global alias.last 'log -1 HEAD'

git last
git st
git ci
git co
(실제 사용 방법)
```

# 3. Git 분기

### 3.1 Git 분기란?
대부분의 VCS에는 주요 개발 라인에서 벗어나, 메인 라인을 수정하지 않고 작업 진행을 지원.

### 3.2 새로운 브랜치 만들기
```
git branch testing
```
이때 현재 commit에 대한 새 포인터가 생성.
![[Pasted image 20230711165910.png]]
git에는 현재 어떤 브랜치에 있는지 알 수 있는 특수 포인터 HEAD를 사용.

### 3.3 브랜치 조회
```console
git log --oneline --decorate
```
![[Pasted image 20230711170313.png]]
![[Pasted image 20230711170329.png]]
```
git checkout testing
git log --oneline --decorate

vi test2.txt
git commit -a -m 'make a change'
git log --oneline --decorate
(testing으로 HEAD를 변경, 브랜치를 commit하여 생성)
```
![[Pasted image 20230711171159.png]]

```
git checkout master
git log --oneline --decorate
(마스터로 다시 돌아옴)
```
![[Pasted image 20230711171313.png]]
![[Pasted image 20230711171329.png]]

```
vi test2.txt
git commit -a -m 'make other changes'
git log --oneline --decorate
(마스터에서 변경사항을 저장하여 브랜치를 남겨놓기)
```
![[Pasted image 20230711171515.png]]
```
git log --oneline --decorate --graph --all
(브랜치와 마스터 모두 조회)
```

### 3.4 브랜치 병합
```
git checkout -b iss1

(위 명령은 아래 명령어를 한번에 실행한 것)
git branch iss1
git checkout iss1

vi issue1
git commit -a -m 'Create new footer [issue 1]'
( 브랜치 iss1을 하나 만들고 파일을 추가)


git checkout -b hotfix
vi issue1
git commit -a -m 'Fix issue file'
( 브랜치 hotfix를 만들고, issue1 파일을 수정.)
 
git checkout master
git merge hotfix```
( master로 체크아웃 후 hotfix를 병합 )

git branch -d hotfix
(병합 후에는 브랜치를 삭제 가능)
```

### 3.5 브랜치 관리
```
git branch
git branch --all
(전체 브랜치 조회)

git branch -v
(전체 브랜치의 마지막 커밋 조회)

git branch --merged
(병합한 브랜치 조회, 만약 git branch -d 로 삭제하면 조회 불가)

git branch --no-merged
(병합되지 않은 브랜치 조회)
```


* 참고자료
https://git-scm.com/book/en/v2/
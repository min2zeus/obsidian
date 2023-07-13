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
- 로컬
	* 대부분의 Git 작업에는 로컬 파일과 리소스만 필요.
	* 네트워크 연결이 되지 않은 상태에서도 작업 가능.
* 무결성
	* Git의 모든 항목은 저장되기전에 체크섬 처리.
	* Git은 파일이름으로 항목을 저장하지 않고 컨텐츠의 해시 값으로 저장.
* 데이터 추가
	* Git에서 작업을 수행하면 모든 작업이 Git 데이터베이스에 데이터 추가.
* 상태
	* Git의 파일은 3가지 ( modified, staged, committed) 상태가 있음.
	* modified는 파일은 변경했지만, 커밋되지 않은 상태.
	* staged는 현재 버전에서 수정된 파일을 다음 커밋 스냅샷으로 이동하도록 표시한 상태.
	* committed는 데이터가 로컬 데이터베이스에 안전하게 저장된 상태.
	![[Pasted image 20230711133559.png]]
	
*  저장 영역
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
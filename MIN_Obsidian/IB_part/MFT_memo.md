---
sticker: emoji//1f47e
########테스트트트
---
---
### FTE 구성 / 설치 디렉토리

opt/IBM/WMQFTE 여기가 기본 디폴트로 WMQFTE 있는곳
안에 

[mqm:mqm@/opt/IBM/WMQFTE]cat install.properties
#Thu Mar 17 14:39:39 KST 2011
dataDirectory=/var/IBM/WMQFTE/config
[mqm:mqm@/opt/IBM/WMQFTE]

cd /var/IBM/WMQFTE/config
cat wmqfte.properties
#
#Mon Apr 19 17:36:20 KST 2010
defaultProperties=COORDQM

저게 조정큐매니져
저거가 있는게 허브이고 

Agent 는 COORDQM 들어가서 agents 본다 
거기있는것들 들어가서 agents.properties 이거보면
에이전트 큐매니져랑 에이전트 네임을 알수있다.

### FTE 설치 구성
FTE 루트로 설치하고 
그리고 조정큐관리자랑 에이전트큐관리자 서로 채널연결상태여야함
브로커같이 트랜스미션큐는 상대큐매니져 이름 그대로
그리고 command.properties는 에이전트큐매니져랑 같은이름으로 하는게 좋다

fteCreate 관련 작업들 같은경우는 
LIBPATH 가 32bit 여야 한다. 근데 mq는 64
그래서 unset 하고 32bit 로 설정을 해줘야함 

mqm 계정
fteSetupCoordination -coordinationQMgr 조정큐매니져 -coordinationQMgrHost 10.10.10.111 -coordinationQMgrPort 1414 -coordinationQMgrChannel SYSTEM.DEF.SVRCONN
fteSetupCommands -connectionQMgr 에이전트큐매니져
fteCreateAgent -agentName 에이전트네임 -agentQMgr 에이전트큐매니져
runmqsc 큐매니져 < 에이전트_create.mqsc

에이전트 시작: fteStartAgent 에이전트
채널 조정이랑 에이전트 서로 XQ는 수신 큐매니져명


BFGCL0255E: An internal error has occurred.  The exception was: 'CC=2;RC=2495;
AMQ8568: The native JNI library 'mqjbnd' was not found. [3=mqjbnd]'. 
Caused by: '/usr/mqm/java/lib/libmqjbnd.so (No such file or directory)'

Could not load program mqrc:
        Dependent module /usr/mqm/lib/libmqz.a(libmqz.o) could not be loaded.
        The module has an invalid magic number.

export LIBPATH=/usr/mqm/lib:$LIBPATH 
export LIBPATH=/usr/mqm/lib64:$LIBPATH 

BFGCL0003E: A messaging problem prevented the command from completing successfully,
for queue SYSTEM.FTE.COMMAND.AGENT on queue manager MIN.
The WebSphere MQ completion code was 2, and the reason code was 2087


BFGCL0033E: A messaging problem prevented the command from completing successfully.
The WebSphere MQ completion code was 2, and the reason code was 2058.
A connection could not be established to queue manager COORDQM.


### MFT config 파일 암호화
MQMFTCredentials.xml 파일에 있는 정보들 난독화
chmod 666 /home/mqm/MQMFTCredentials.xml
fteObfuscate -credentialsFile /home/mqm/MQMFTCredentials.xml

qm.ini에 있는 정보들 암호화
 setmqxacred 명령 사용 (8.0.0.4 이상)
 setmqxacred -m CORDQM -x FTELOGDB -u dtt -p dtt
qm.ini 의 user 부분에 +USER+ password 부분에 +PASSWORD+ 설정
 setmqxacred -m CORDQM -x FTELOGDB -d 삭제
비번에 특수문자있을경우
 setmqxacred -m CORDQM -x FTELOGDB -u dtt -p dtt'!''!'
 

### Windows Agent 가 fteListAgents. 안보일때
agent.properties 에 등록
publicationMDUser=mqm

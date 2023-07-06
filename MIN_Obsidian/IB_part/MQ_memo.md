---
sticker: emoji//1f431
---
 
 ---
 
### CCDT 구성 테스트
SVRCONN 채널과 같은 이름의 CLNTCONN 채널 생성
CLNTCONN 채널의 conname 은 서버의 아이피와 포트
그리고 qmname 넣어 줘야함
[[markdown-cheat-sheet]]
[[[[[[[[markdown-cheat-sheet]]]]]]]]

디폴트인 /var/mqm/ 에 넣어주면 

MQCHLLIB export 안해도 됨  
파일명도 디폴트면 MQCHLTAB export 안해도 됨

export MQCHLLIB=/var/mqm/
export MQCHLTAB=AMQCLCHL.TAB
export MQCCSID=


export MQSERVER=TO.1.PTGWT/TCP/'10.10.11.49(8484)'
export MQCCSID=970
export MQSERVER=SYSTEM.DEF.SVRCONN/TCP/'10.10.11.49(6464)'

export MQCHLLIB=/home/mqm/min
export MQCHLTAB=AMQCLCHL.TAB
export MQCCSID=970

runmqtmc -q TEST2.IQ -m MINTEST &


10.10.1.148 (active)
runmqsc MINTEST 
DEFINE CHANNEL(ALPHA) CHLTYPE(SVRCONN) TRPTYPE(TCP) MCAUSER('mqm')
DEFINE CHANNEL(ALPHA) CHLTYPE(CLNTCONN) TRPTYPE(TCP) CONNAME('10.10.1.148(6464)') QMNAME(MINTEST) 
DEFINE CHANNEL(BETA) CHLTYPE(CLNTCONN) TRPTYPE(TCP) CONNAME('10.10.1.149(6464)') QMNAME(MINTEST) 
DEFINE QLOCAL(Q1.LQ)
 
10.10.1.149 (stand-by)
runmqsc MINTEST 
DEFINE CHANNEL(BETA) CHLTYPE(SVRCONN) TRPTYPE(TCP) MCAUSER('mqm')
DEFINE QLOCAL(Q1.LQ)

10.10.1.129
export MQCHLLIB=/home/mqm/min/CCDT
export MQCHLTAB=AMQCLCHL.TAB
export MQCCSID=949

### CCSID 사용
두 서버에서 사용하는 ccsid값이 같은 byte를 사용해야만 연결이 된다.
1 byte : 819(unix), 437(Windows), 500(Host)
2 bytes : 970(unix), 949(Windows), 933(Host)

### Client 채널 끊기 (clientidle)
Windows
 Use the following command: 
amqmdain reg <qm_name> -c add -s Channels -v ClientIdle=<val_in_secs>

UNIX and iSeries 
Set in the QM.ini file using a new stanza, as follows:
 Channels: 
ClientIdle=<val_in_secs>
 
Note: On all operating systems, you must restart the queue manager for the parameter to take effect . 
http://www-01.ibm.com/support/docview.wss?rs=171&context=SSFKSJ&context=SSEP7X&q1=clientidle&uid=swg21376219&loc=en_US&cs=utf-8&lang=en

### HA 구성
엔진 - /opt /enge001 은 HA 이동 필요 없음 
구성쪽만 설정

Active - Standby

Active 서버에서
/var/mqm mv 로 /data001/mqm 으로 옴긴담에 링크

mv mqm /data001/
ln -s /data001/mqm mqm

/var/mqm  Standby 서버에서 
mqm 지우고
/data001/mqm 링크 걸어 놓기만 하면 됨
ln -s /data001/mqm mqm

=======================================================================================


Active - Active

각각 큐매니져 만들고 
/var/mqm/mqs.ini 수정 1번 서버는 기본정보에 2번 서버 정보추가 
2번 서버는 기본정보에 1번 서버 정보추가
###########  엠큐 링크 설정  ###########
 
/var/mqm mv 로 /data001/mqm 으로 옴긴담에 링크

mv mqm /data001/
ln -s /data001/mqm mqm

###########  브로커 링크 설정 ###########

/data001/mqsi/components/mb1
/data001/mqsi/registry/mb1
/data001/mqsi/components/mb2  ->   /data002/mqsi/components/mb2
/data001/mqsi/registry/mb2   ->   /data002/mqsi/registry/mb2

2번 서버는 반대로

/data002/mqsi/components/mb2
/data002/mqsi/registry/mb2
/data002/mqsi/components/mb1  ->   /data001/mqsi/components/mb1
/data002/mqsi/registry/mb1   ->   /data001/mqsi/registry/mb1

### HBINT 관련 설명
센더 채널 러닝 

그러면 디스 0 이면 
수신이 사용할수있는상태 인지 체크 
수신측이 사용가능한상태인지 센더가 확인하는 시간 간격이 하트비트간격
줄이면 영향도는  수신측입장에서는 내상태가 괜찮은지 TCP 소켓쪽으로 체크하는 게 많아진다고 
근본적으로 네트워크문제 

하트비트 간격(HBINT)

트랜스미션 큐에 메시지가 없을 때 송신 MCA에서 전달되는 하트비트 플로우 사이의 대략적인 시간을 지정할 수 있습니다.
하트비트 플로우는 메시지가 도달하기를 기다리거나 연결해제 간격이 만료되기를 기다리는 수신 MCA를 블록해제합니다.
수신 MCA가 블록해제되면, 연결해제 간격이 만료되길 기다리지 않고 채널을 연결해제할 수 있습니다.
 하트비트 플로우는 또한 큰 메시지에 할당된 기억 영역 버퍼를 해제하고 채널의 수신측에 열려 있는 모든 큐를 닫습니다.
 
이 값의 단위는 초이며 그 범위는 0-999,999이어야 합니다. 0의 값은 어떤 하트비트 플로우도 송신되지 않았음을 의미합니다.
 디폴트 값은 300입니다. 가장 효과적이기 위해서는 값이 연결해제 간격 값보다 상당히 낮아야 합니다.

서버-연결 및 클라이언트-연결 채널에서는 하트비트는 서버나 클라이언트 측 모두에서 독립적으로 흐를 수 있습니다.
 하트비트 간격 동안 채널에 전송되는 데이터가 없으면 클라이언트-연결 MQI 에이전트가 하트비트 플로우를 전송하고
 서버-연결 MQI 에이전트는 다른 하트비트 플로우로 이에 응답합니다. 이때 채널의 상태와 상관없이 하트비트 플로우를
 전송합니다(예: API 호출로 인한 채널의 비활성화, 클라이언트 사용자의 입력 대기로 인한 비활성화).
 서버-연결 MQI 에이전트는 위에서 언급된 것처럼 채널의 상태와 상관없이 클라이언트에 하트비트를 전송할 수 있습니다.
 서버-연결 및 클라이언트-연결 MQI 에이전트가 동시에 서로 하트비트를 전송하는 것을 방지하기 위해, 하트비트 간격
 더하기 5초 동안 채널에 전송되는 데이터가 없는 경우 서버 하트비트가 전송됩니다.
 
WebSphere MQ 버전 7.0 이전의 채널 모드에서 작동하는 서버 연결 및 클라이언트 연결 채널의 경우,
 서버 MCA가 클라이언트 응용프로그램 대신 발행한, WAIT 옵션이 지정된 MQGET 명령을 기다리는 경우에만 하트비트가 플로우됩니다. 

두 모드에서 MQI 채널을 만드는 것 관련 자세한 내용은 SharingConversations(MQLONG)를 참조하세요.

### HP itanium 11.31에서 MQ V6 설치 방법

MQ v6.0.2.2 이하에서는 sam에서 gskit만 보임
따라서 아래 명령으로 install 

1. 원본 설치 명령
swinstall -s FullPATH/{installable-image} -x allow_incompatible=true
ex) swinstall -s /data1/inst/wmq/p600-100-051021.v11 -x allow_incompatible=true 

2. 설치 후 아래 명령 실행 (아래 명령을 수행하기 전에는 MQ 명령어나 fix 적용이 안됨)
swconfig -x allow_incompatible=true MQSERIES 
(--> fix 업그레이드 하기전에 실행하기)

3. 패치 설치 명령
swinstall -s FullPATH/{installable-image} -x allow_incompatible=true 
ex) swinstall -s /data1/inst/wmqcsd/hp-U814342.v11 -x allow_incompatible=true 

4. 설치 후 확인 명령
 swlist -v -a state -l fileset MQSIRIES  
 --> configured 상태 확인

아래 url 참조
http://www-01.ibm.com/support/docview.wss?uid=swg1IV00114



### HP MQ 강제 제거

swremove -X env.txt MQSERIES,l=/opt/mqm,r=7.0.1.10

swremove MQSERIES,l=/opt/mqm,r=7.1.0.0
swremove -X env.txt MQSERIES,l=/opt/mqm,r=7.1.0.0
swremove -X env.txt MQSERIES,l=/opt/mqm,r=7.0.1.9
swremove -X env.txt MQSERIES,l=/opt/mqm,r=7.0.0.0	

swremove -X env.txt MQSERIES.MQM-CL-HPUX,l=/opt/mqm7.1,r=7.1.0.0
swremove -X env.txt MQSERIES.MQM-RUNTIME,l=/opt/mqm7.1,r=7.1.0.0 
swremove MQSERIES.MQM-RUNTIME,l=/opt/mqm7.1,r=7.1.0.0

MQSERIES.MQM-BASE-U853087

env.txt 안에 아래 값 설정

enforce_dependencies=false
enforce_scripts=false

swremove -X env.txt MQSERIES.MQM-BASE,l=/opt/mqm,r=7.5.0.2    
swremove -X env.txt MQSERIES.MQM-CL-HPUX,l=/opt/mqm,r=7.5.0.2 
swremove -X env.txt MQSERIES.MQM-JAVA,l=/opt/mqm,r=7.5.0.2    
swremove -X env.txt MQSERIES.MQM-JAVAJRE,l=/opt/mqm,r=7.5.0.2 
swremove -X env.txt MQSERIES.MQM-MAN,l=/opt/mqm,r=7.5.0.2     
swremove -X env.txt MQSERIES.MQM-SAMPLES,l=/opt/mqm,r=7.5.0.2 
swremove -X env.txt MQSERIES.MQM-SERVER,l=/opt/mqm,r=7.5.0.2  


swremove -X env.txt MQSERIES,l=/opt/mqm7.5,r=7.5.0.0


### ipcrm 정리
ipcs | grep mqm | awk '{print "ipcrm -"$1,$2}'

1. 서버 큐매니져 종료
   endmqm -i EAI1P
   endmqm -i EAI2P  (종료되어 있어서 따로 안해줘도 될것으로 보임)
   endmqm -i MTEP


2. IPC 자원 정리
   ipcs | grep mqm | awk '{print "ipcrm -"$1,$2}'

  위의 명령어를 입력하면 출력되는 ipcrm -m 12312312, ipcrm -s 123123 등
  내용을 명령창에서 실행 

ex)
ipcrm -m 16777223
ipcrm -m 16777222
ipcrm -m 16777221
ipcrm -m 16777220
ipcrm -m 16777219
ipcrm -m 16777218
ipcrm -m 16777217
ipcrm -m 127
ipcrm -m 126
ipcrm -m 0
ipcrm -s 39
ipcrm -s 38
ipcrm -s 37
ipcrm -s 36
ipcrm -s 35
ipcrm -s 34
ipcrm -s 33

  IPC 자원 정리 후 ipcs | grep mqm 을 통한 남아있는 자원 확인
  남아있으면 ipcrm 명령을 통해서 지워준다.

  ※ mqsi 자원도 남아있으면 브로커 서비스 종료 후 같이 정리해줘도 됨

3. IPC 자원 정리 후 큐매니져 시작
  strmqm EAI1P
  strmqm EAI2P
  strmqm MTEP

### KeepAlive / HBINT / KAINT 설명
KeepAlive
채널의 다른 쪽이 사용 가능한지 점검 (하트비트 간격, 활성 유지(Keep Alive) 간격 및 수신 시간 종료를 사용하여)
TCP가 주기적으로 연결의 다른 쪽이 여전히 사용 가능한지 점검하고, 만약 사용 불가능하다면 채널이 종료됩니다
TCP 오류가 발생하며 신뢰할 수 없는 채널을 가지고 있는 경우에는 KEEPALIVE를 사용하여 채널이 잘 복구되게 할 수 있습니다.
시간 간격을 지정하여 KEEPALIVE 옵션의 작동을 제어할 수 있습니다.
시간 간격을 변경하면, 변경 후에 시작된 TCP/IP 채널만이 영향을 받습니다.
시간 간격을 위해 선택한 값은 채널의 연결해제 간격에 대한 값보다 작아야 합니다


HBINT(하트비트)
하트비트 간격 채널 속성을 사용하여 트랜스미션 큐에 메시지가 없을 때 송신 MCA에서 전달되는
하트비트 플로우 사이의 대략적인 시간을 지정할 수 있습니다


KAINT(활성 유지 간격)
채널에 시간 종료 값을 지정하는 데 사용됩니다.

채널이 내려간다 ? 그럼 전체 runmqtmc가 내려가게 되는건데 ..
아니다 채널 그거 관련된 채널만 프로세스도 관련 amqzlaa0 이 에이전트 프로세스만 죽는거임

TCPKeepAlive 속성의 부재로 인해 Server와 Client 간의 연결을 Client가 인지하지 못하여 
connectionless? 가 일어난 것으로 보인다.
해당 KeepAlive 속성을 줌으로 해서 해결하고 다른해결방안으로는 온라인성으로 띄우고 polltime을
30000?  30초정도로 해서 사용 

NH에 실제로 0으로 주고 사용하는 인터페이스도 있다(NT) 


### MQ OBJ 백업
dmpmqcfg -a -t all -o mqsc -m 큐매니저명 > 백업파일명.mqs



### MQ Handle_Conn 확인 스크립트
-handle_check.sh 사용법 
(변수 설정)
QMGR=큐관리자명
Q_IN_HANDLE_LIMIT=설정된 개수 이상 in 핸들 사용 시 큐이름 출력
Q_OUT_HANDLE_LIMIT=설정된 개수 이상 out 핸들 사용 시 큐이름 출력
PROC_CONN_LIMIT=설정된 개수 이상 CONN 사용하는 PID 및 프로세스명 출력
(참고) 아래 부분은 수정 필요 없음.
(참고) hp-ux 가 아닌 경우 ps -efx 를 ps -ef 로 변경
hpux31:[/home/mqm/djs] cat handle_check.sh
#!/bin/ksh
QMGR=FIS
Q_IN_HANDLE_LIMIT=1
Q_OUT_HANDLE_LIMIT=1
PROC_CONN_LIMIT=1

======

#!/bin/ksh
QMGR=FIS
Q_IN_HANDLE_LIMIT=1
Q_OUT_HANDLE_LIMIT=1
PROC_CONN_LIMIT=1
?
#################### IPPROCS
?
echo ""
echo ===== IPPROCS check : more then $Q_IN_HANDLE_LIMIT =====
?
Q_LIST=`echo "dis qs(*) where (ipprocs ge $Q_IN_HANDLE_LIMIT) ipprocs" | runmqsc $QMGR`
?
for VAL in $Q_LIST
do
  if [ `echo $VAL | grep -c QUEUE\(` -eq 1 ]
  then
    Q_NAME=`echo $VAL | awk -F\( '{print $2}' | awk -F\) '{print $1}'`
  elif [ `echo $VAL | grep -c IPPROCS` -eq 1 ]
  then
    echo $Q_NAME : $VAL
	#SEND_SMS
  fi
done
?
#################### OPPROCS
?
echo ""
echo ===== OPPROCS check : more then $Q_OUT_HANDLE_LIMIT =====
?
Q_LIST=`echo "dis qs(*) where (opprocs ge $Q_OUT_HANDLE_LIMIT) opprocs" | runmqsc $QMGR`
?
for VAL in $Q_LIST
do
  if [ `echo $VAL | grep -c QUEUE\(` -eq 1 ]
  then
    Q_NAME=`echo $VAL | awk -F\( '{print $2}' | awk -F\) '{print $1}'`
  elif [ `echo $VAL | grep -c OPPROCS` -eq 1 ]
  then
    echo $Q_NAME : $VAL
	#SEND_SMS
  fi
done
?
#################### QMGR CONN
?
echo ""
echo ===== QMGR CONN check : more then $PROC_CONN_LIMIT =====
?
PID_LIST=`echo "dis CONN(*) pid" | runmqsc $QMGR | grep PID | sort | uniq -c | awk '$1 >= "'"$PROC_CONN_LIMIT"'" { print $0 }'`
for VAL in $PID_LIST
do
  if [ `echo $VAL | grep -c PID\(` -eq 1 ]
  then
    PID_NUM=`echo $VAL | awk -F\( '{print $2}' | awk -F\) '{print $1}'`
	echo CONN_CNT : $PID_COUNT / PID : $PID_NUM / `ps -efx | awk '$2 == "'"$PID_NUM"'" { print $0 }'`
	#SEND_SMS
  else
    PID_COUNT=$VAL
  fi

  
### MQ v5 설치시 주의사항

AIX - v6는 64bit  v5는 32bit qm.ini에서 차이가 있다 쓰는 lib

v6 - Module=/usr/mqm/lib64/amqzfu
v5 - Module=/usr/mqm/lib/amqzfu

저 부분이 제대로 설정 안되면 MQRC_NOT_AUTHORIZED (2035 에러) 떨어짐 


그리고 mqs.ini에서 

Prefix=/var/mqm (Default) 인데
Prefix=/mqm_fix/mqdata  이런식으로 바껴져 있다면

/var/mqm/qmgrs 에 있는 큐매니져자체를 mv 
/mqm_fix/mqdata/qmgrs/큐매니져   위치로 이동

qm.ini에 있는 logpath도 보고 맞게 설정
/mqm_fix/mqlog/큐매니져    위치로 이동

-ld로 설정을 해줄라면 -ld /mqm_fix/mqlog 까지만 뒤에 큐매니져명은 없어야 함

설치 후 setmqcap 작업을 해야함 맞는 CPU???? CU??? 값을 설정
setmqcap 16   ->   시스템 CPU가 16개인 경우



Windows - 설치시 MQ 설치하고 CSD 패치 설치 할때 MQ 설치창 마침 누르지 않은 상태에서 CSD 패치
그래야 정상적으로 설치 가능
설치후 리스너, 채널 시작기, 트리거 있으면 설정
그리고 로그오프해도 큐매니져 정상동작하기 위해선 IBM 서비스에 다음사용자로 로그온에서 
맞는 로그인계정 설정 

프로세스가 v5는 좀 많이 뜬다
정상동작안하면 mq 관련 프로세스 종료 후 다시 시작


### MQ Windows qm.ini
HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\IBM\MQSeries\CurrentVersion\Configuration\QueueManager\MINTEST\Log


### MQ 설치계정 root 인 이유
1. IBM 설치 권고사항
2. MQ 설치할때 시스템 LIB를 참조 / 공유하는 부분이 있어 ROOT 권한으로 설치가 진행되어야 함
3. MQM 계정으로 설치 진행시 에러발생 설치안됨
솔라리스의 경우 setuid를 사용해야하고 시스템 참조 공유 root 필요

### MQM 프로세스

 amqzfuma - 오브젝트 권한 관리자, OAM 프로세스 
 amqrmppa - 채널 프로세스 풀링 작업 
 amqpcsea - 명령 서버, dspmqcsv 확인해서 안돌고 있으면 프로세스 안떠있을듯 5.3 은 strmqm이랑
            strmqcsv 따로 실행해야함 탐색기에 큐관리자 시작하면 자동실행해주는게 있다곤 함
 amqrrmfa - 클러스터 저장소 관리자
 amqzmgr0 - 프로세스 컨트롤러
 amqzdmaa - 지연 메시지 관리자
 amqzlaa0 - 에이전트, 다른거들이 잡고있는거 관련 프로세스, 64 스레드? 늘어남 뒤에잡고있는 fip0 ~ X 보면 됨
            수동으로는 구동이 안됨(엔진에서) dis conn(*) 봐서 / 64 + 2~3 정도가 떠있는게 정상
 amqzxma0 - 실행 컨트롤러, 부모 프로세스, 엔진 프로세스, 이게 안떠있음 다 안됨
 amqzmuf0 - 유틸리티 관리자
 amqzmuc0 - 유틸리티 관리자
 amqzmur0 - 유틸리티 관리자
 runmqchl - 송신채널에서 생기는 프로세스 
 runmqchi - 채널 시작기, 하나 뜸 꼭 있어야 함 
 runmqlsr - 리스너

 amqhasmx - 로거  
 amqharmx - 로그 형식기(선형 로깅만)  
 amqzllp0 - 체크포인트 프로세서  
 amqmsrvn

DCOM object configured with interactive/launching user id
configure DCOM objwct to use specific user id

INCORROUT

### MQSPREFIX 설정
export MQSPREFIX=/설정/설정

해놓으면 mqs.ini prefix 저절로 저위치로 해서 만듬
/var/mqm/log 이위치는 -ld 로 해서 하면 됨

그리고 MQ v7 버전은 프리픽스 -md 이옵션으로 해서 하면됨

MQ v5.3 / v6.0
MQSPREFIX=D:\mqm\data crtmqm -lp 3 -ls 2 -lf 16384 -ld D:\mqm\log EAIBK3P

MQ v7.0
crtmqm -md D:\mqm\data -lp 3 -ls 2 -lf 16384 -ld D:\mqm\log EAIBK3P

### NH 트리거 프로세스 쉘
echo "dis process(*)" | runmqsc MINTEST | grep PROCESS > process.list.txt

for i in `process.list.txt`
do
PRONAME=`echo $i | awk -F\( {'print $2'}`
IQ=`echo "dis ql(*) initq where (PROCESS eq $PRONAME" | runmqsc MINTEST | grep INITQ`
APPID=`echo "dis process($PRONAME applicid" | runmqsc MINTEST | grep APPLICID`
echo $i $IQ $APPID
done

for i in $(cat process.txt)
do
PRONAME=`echo $i | awk -F\( {'print $2'}`
IQ=`echo "dis ql(*) initq where (PROCESS eq $PRONAME" | runmqsc MINTEST | grep INITQ`
APPID=`echo "dis process($PRONAME applicid" | runmqsc MINTEST | grep APPLICID`
echo $i $IQ $APPID
done

PRONAME=TEST2.PRO\)
echo "dis ql(*) all where (PROCESS eq $PRONAME" | runmqsc MINTEST
echo "dis process($PRONAME applicid all" | runmqsc MINTEST

   PROCESS(TEST.PRO)
   PROCESS(TEST2.PRO)

echo "Subdir in `pwd`:"
for file in `ls`
do
    if [ -d $file ]
    then
        echo $file
    fi
done

### qdep 디렉토리 쉘
QMGR=TEST
TODAY=`expr $(date +%y%m%d)`
QDIR=/var/mqm/qmgrs/$QMGR/queues
cd ${QDIR}

QLIST=`ls | grep -v SYSTEM | grep -v SVRCONN | grep -v GHOST `

QLIST=`echo $QLIST | sed 's/!/./g'`
echo $QLIST
while true
do
echo "****************************************"
date "+%D %H:%M:%S" 
echo "****************************************"
for i in $QLIST
do
        QUEUE=`echo $i | sed 's/!/./g'`
        DEPTH=`echo "dis q(${QUEUE}) WHERE(CURDEPTH ne 0)" | runmqsc ${QMGR} | grep -v "dis q(${QUEUE}) WHERE(CURDEPTH ne 0)" | grep CURDEPTH | sed s^CURDEPTH\(^^g | sed s^\)^^g`
#echo "dis q(EVQ) WHERE(CURDEPTH ne 0)" | runmqsc TEST | grep -v "dis q(EVQ) WHERE(CURDEPTH ne 0)" | grep CURDEPTH | sed s^CURDEPTH\(^^g | sed s^\)^^g
        if [ "$DEPTH" ]; then
         echo "${QUEUE} ${DEPTH}"
        fi
done
sleep 2
done

### qdep 큐목록 쉘
#!/usr/bin/ksh
export LANG=C

QMGR=$1
QMGRLIST=$1

if [[ $# -lt 1 ]]
then
        echo " "
        echo "usage : $0  [ QMGR Name]"
        echo "\t QMGR Name     : Blank is Default QMGR"
        echo " "
        qdep TEST
        exit 0
fi
while true
do
  echo "*************************************************"
  echo "*\t"`date "+DATE: %m/%d/%y TIME: %H:%M:%S"`"\t\t*"
  echo "*************************************************"
     echo "*************************************************"
    echo "*\t\tQMGR : $QMGR\t\t\t*"
    echo "*************************************************"
      QDEP_LIST=`echo "dis ql(*) curdepth where(curdepth gt 0)" | runmqsc $QMGR | egrep "QUEUE|CURDEPTH"`
     for QDEP in $QDEP_LIST
     do
       ATTR=`echo $QDEP | cut -d"(" -f 1`
       ATTR_VAL=`echo $QDEP | cut -d"(" -f 2 | cut -d")" -f 1`
       if [ "$ATTR" == "QUEUE" ]
       then
         ECHO_STR="$QDEP"
         QNAME=$ATTR_VAL
       else
         if [ "$ATTR" == "CURDEPTH" ]
         then
           #echo `echo $QNAME | wc -c`
           if [[ `echo $QNAME | wc -c` -gt 34 ]]
           then
             ECHO_STR="$ECHO_STR\t$QDEP"
           elif [[ `echo $QNAME | wc -c` -gt 25 ]]
           then
             ECHO_STR="$ECHO_STR\t\t$QDEP"
           elif [[ `echo $QNAME | wc -c` -gt 17 ]]
           then
             ECHO_STR="$ECHO_STR\t\t\t$QDEP"
           elif [[ `echo $QNAME | wc -c` -gt 9 ]]
           then
             ECHO_STR="$ECHO_STR\t\t\t\t$QDEP"
           elif [[ `echo $QNAME | wc -c` -le 9 ]]
           then
             ECHO_STR="$ECHO_STR\t\t\t\t\t$QDEP"
           else
             ECHO_STR="$ECHO_STR\t\t\t\t\t$QDEP"
           fi
             echo $ECHO_STR | grep -v SYSTEM
         fi
       fi
     done
sleep 3
done

### MQ SSL 인증서 구성 참고 자료

인증서 유효기간 설정
gsk7cmd(gsk7cmd_64) -cert -create -db "/var/mqm/qmgrs/큐매니저명/ssl/key.kdb" -pw 암호 -label ibmwebspheremq로그인ID(소문자) -dn "SSLPEER 식별이름 패턴" -expire 7300
 
인증서 유효기간 확인하는 방법
gsk7cmd(gsk7cmd_64) -cert -list -expiry -db /var/mqm/qmgrs/EAI01DX/ssl/key.kdb -pw 1234


**

export JAVA_HOME=/home/was9/IBM/java

1. Key DB 생성
gsk7cmd_64 -keydb -create -db "/var/mqm/qmgrs/CORDQM/ssl/key.kdb" -pw 123 -type CMS -stash
gsk7cmd_64 -keydb -create -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -type CMS -stash
gsk7cmd_64 -keydb -create -db "/home/mqm/ssl/truststore.jks" -pw 123 -type JKS -stash

2. 인증서 생성
gsk7cmd_64 -cert -create -db "/var/mqm/qmgrs/CORDQM/ssl/key.kdb" -pw 123 -label ibmwebspheremqcordqm -dn "CN=FTEUser,O=IBM,C=KR" -size 1024 -x509version 3 -expire 7300
gsk7cmd_64 -cert -create -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -label ibmwebspheremqfte2p -dn "CN=FTEUser,O=IBM,C=KR" -size 1024 -x509version 3 -expire 7300


3. 인증서 추출
gsk7cmd_64 -cert -extract -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -label ibmwebspheremqfte2p -target  "/home/mqm/ssl/FTE2P.der" -format binary
gsk7cmd_64 -cert -extract -db "/var/mqm/qmgrs/CORDQM/ssl/key.kdb" -pw 123 -label ibmwebspheremqcordqm -target  "/home/mqm/ssl/CORDQM.der" -format binary


4.상대방 인증서 교환 및 등록
gsk7cmd_64 -cert -add -db "/home/mqm/ssl/truststore.jks" -pw 123 -type jks -label ibmwebspherecordqm -file "/home/mqm/ssl/CORDQM.der" -format binary -trust enable
gsk7cmd_64 -cert -add -db "/home/mqm/ssl/truststore.jks" -pw 123 -type jks -label ibmwebspherefte2p -file "/home/mqm/ssl/FTE2P.der" -format binary -trust enable


5. 큐매니저 SSL 등록
6. 채널 SSL 등록
define channel(FTE2P.SSL.SVRCONN) chltype(CLNTCONN) trptype(TCP) sslciph(RC4_MD5_EXPORT) conname('10.10.11.44(1617)') qmname(FTE2P)
define channel(CORDQM.SSL.SVRCONN) chltype(CLNTCONN) trptype(TCP) sslciph(RC4_MD5_EXPORT) conname('10.10.11.44(1616)') qmname(CORDQM)

인증서 유효기간 설정
gsk7cmd(gsk7cmd_64) -cert -create -db "/var/mqm/qmgrs/큐매니저명/ssl/key.kdb" -pw 암호 -label ibmwebspheremq로그인ID(소문자) -dn "SSLPEER 식별이름 패턴" -expire "설정할 유효기간 날짜 수"
 
인증서 유효기간 확인하는 방법
gsk7cmd(gsk7cmd_64) -cert -list -expiry -db /var/mqm/qmgrs/MINTEST/ssl/key.kdb -pw 123

gsk7cmd_64 -cert -list -expiry -db /var/mqm/qmgrs/MINTEST/ssl/key.kdb -pw 123

gsk7cmd_64 -cert -list -expiry -db /home/mqm/min/ssl/truststore.jks -pw 123

gsk7cmd_64 -cert -list -expiry -db /home/mqm/min/ssl/truststore.jks -pw 123


export JAVA_HOME=/home/was9/IBM/java

1. Key DB 생성
gsk7cmd_64 -keydb -create -db "/var/mqm/qmgrs/MINTEST/ssl/key.kdb" -pw 123 -type CMS -stash
gsk7cmd_64 -keydb -create -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -type CMS -stash
gsk7cmd_64 -keydb -create -db "/home/mqm/min/ssl/truststore.jks" -pw 123 -type JKS

2. 인증서 생성
gsk7cmd_64 -cert -create -db "/var/mqm/qmgrs/MINTEST/ssl/key.kdb" -pw 123 -label ibmwebspheremqcordqm -dn "CN=FTEUser,O=IBM,C=KR" -size 1024 -x509version 3
gsk7cmd_64 -cert -create -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -label ibmwebspheremqfte2p -dn "CN=FTEUser,O=IBM,C=KR" -size 1024 -x509version 3


3. 인증서 추출
gsk7cmd_64 -cert -extract -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -label ibmwebspheremqfte2p -target  "/home/mqm/ssl/FTE2P.der" -format binary
gsk7cmd_64 -cert -extract -db "/var/mqm/qmgrs/MINTEST/ssl/key.kdb" -pw 123 -label ibmwebspheremqcordqm -target  "/home/mqm/min/ssl/CORDQM.der" -format binary


4.상대방 인증서 교환 및 등록
gsk7cmd_64 -cert -add -db "/home/mqm/min/ssl/truststore.jks" -pw 123 -type jks -label ibmwebspherecordqm -file "/home/mqm/min/ssl/CORDQM.der" -format binary
gsk7cmd_64 -cert -add -db "/home/mqm/ssl/truststore.jks" -pw 123 -type jks -label ibmwebspherefte2p -file "/home/mqm/ssl/FTE2P.der" -format binary -trust enable


5. 큐매니저 SSL 등록
6. 채널 SSL 등록
define channel(FTE2P.SSL.SVRCONN) chltype(CLNTCONN) trptype(TCP) sslciph(RC4_MD5_EXPORT) conname('10.10.11.44(1617)') qmname(FTE2P)
define channel(CORDQM.SSL.SVRCONN) chltype(CLNTCONN) trptype(TCP) sslciph(RC4_MD5_EXPORT) conname('10.10.11.44(1616)') qmname(CORDQM)

1. Key DB 생성
gsk7cmd_64 -keydb -create -db "/var/mqm/qmgrs/MINTEST/ssl/key.kdb" -pw 123 -type CMS -stash
gsk7cmd_64 -keydb -create -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -type CMS -stash
gsk7cmd_64 -keydb -create -db "/home/mqm/min/ssl/truststore.jks" -pw 123 -type JKS -stash

2. 인증서 생성
gsk7cmd_64 -cert -create -db "/var/mqm/qmgrs/MINTEST/ssl/key.kdb" -pw 123 -label ibmwebspheremqmintest -dn "CN=FTEUser,O=IBM,C=KR" -size 1024 -x509version 3 -expire 7300
gsk7cmd_64 -cert -create -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -label ibmwebspheremqfte2p -dn "CN=FTEUser,O=IBM,C=KR" -size 1024 -x509version 3 -expire 7300


3. 인증서 추출
gsk7cmd_64 -cert -extract -db "/var/mqm/qmgrs/FTE2P/ssl/key.kdb" -pw 123 -label ibmwebspheremqfte2p -target  "/home/mqm/min/ssl/FTE2P.der" -format binary
gsk7cmd_64 -cert -extract -db "/var/mqm/qmgrs/MINTEST/ssl/key.kdb" -pw 123 -label ibmwebspheremqmintest -target  "/home/mqm/min/ssl/MINTEST.der" -format binary


4.상대방 인증서 교환 및 등록
gsk7cmd_64 -cert -add -db "/home/mqm/min/ssl/truststore.jks" -pw 123 -type jks -label ibmwebspheremintest -file "/home/mqm/min/ssl/MINTEST.der" -format binary -trust enable
gsk7cmd_64 -cert -add -db "/home/mqm/min/ssl/truststore.jks" -pw 123 -type jks -label ibmwebspherefte2p -file "/home/mqm/min/ssl/FTE2P.der" -format binary -trust enable



### Active 로그 망가진 큐매니져 복구 방법
strmqm 안되는 큐매니져 복구할라면 
원래 큐매니져(qm.ini)와 -lp -ls -lf 똑같은 
TEST 큐매니져를 만들고
TEST 큐매니져의 LogPath /var/mqm/log/TEST/ 
밑에 있는 active, amqhlctl.lfh를 원래 큐매니져의 LogPath 위치에 덮어쓴다
cp -R a* /var/mqm/log/원래큐매니져/
위의 방식으로 그리고 다시 strmqm 해보자

cat *1* | grep AMQ |sort |uniq |more


### 도메인 계정 MQ 권한
도메인계정으로 큐매니져 로그디렉토리나 데이터 디렉토리 만들려고할떄 권한에러 
해당 mqm 그룹권한을 상위디렉토리에 포함하여서
그리고 runmqsc 권한 에러는 서비스의  MQ Series 서비스 실행 계정을 도메인 계정으로 해야함



### MQ 디폴트 설정

alt qmgr ccsid(1208)
alt qmgr deadq(DEAD.DQ)
alt qmgr chlauth(DISABLED)
alt CHANNEL(SYSTEM.DEF.SVRCONN)             CHLTYPE(SVRCONN) MCAUSER('mqm')
alt CHANNEL(SYSTEM.DEF.SVRCONN)             CHLTYPE(SVRCONN) MCAUSER('MUSR_MQADMIN1')
alt CHANNEL(SYSTEM.BKR.CONFIG)              CHLTYPE(SVRCONN) MCAUSER('MUSR_MQADMIN1')
alt LISTENER(SYSTEM.DEFAULT.LISTENER.TCP)   TRPTYPE(TCP) CONTROL(QMGR) PORT(3435)
alt ql(SYSTEM.DEFAULT.LOCAL.QUEUE) DEFPSIST(YES) MAXDEPTH(999999999) MAXMSGL(104857600) PROPCTL(FORCE)


- MQ 7.1 이상
alter qmgr CHLAUTH(DISABLED)

- MQ 8.0 이상
ALTER AUTHINFO(SYSTEM.DEFAULT.AUTHINFO.IDPWOS) AUTHTYPE(IDPWOS) CHCKLOCL(NONE) CHCKCLNT(NONE)
REFRESH SECURITY TYPE(CONNAUTH)

- MQ 기본
def ql(DEAD.DQ) MAXMSGL(104857600) MAXDEPTH(999999999) DEFPSIST(YES)
alter qmgr CCSID(1208)
alter qmgr deadq(DEAD.DQ)
alter qmgr MAXMSGL(104857600)
def LISTENER(SYSTEM.DEFAULT.LISTENER.TCP)  TRPTYPE(TCP) CONTROL(QMGR)  PORT(1414) replace
def CHANNEL(SYSTEM.DEF.SVRCONN)  CHLTYPE(SVRCONN) MAXMSGL(104857600) MCAUSER('mqm') replace
def CHANNEL(SYSTEM.ADMIN.SVRCONN)  CHLTYPE(SVRCONN) MAXMSGL(104857600) MCAUSER('mqm') replace


빨강색 상황에 맞게 변경하여 사용
리스너, CCSID 는 큐관리자 재기동 후 반영


crtmqm -lp 10 -ls 5 -lf 65535 WIN47_JUN
strmqm WIN47_JUN

def ql(DEAD.DQ) MAXMSGL(104857600) MAXDEPTH(999999999) DEFPSIST(YES)
alter qmgr CCSID(1208)
alter qmgr deadq(DEAD.DQ)
alter qmgr MAXMSGL(104857600)
def LISTENER(SYSTEM.DEFAULT.LISTENER.TCP)  TRPTYPE(TCP) CONTROL(QMGR)  PORT(6902) replace
def CHANNEL(SYSTEM.DEF.SVRCONN)  CHLTYPE(SVRCONN) MAXMSGL(104857600) MCAUSER('MUSR_MQADMIN') replace
def CHANNEL(SYSTEM.ADMIN.SVRCONN)  CHLTYPE(SVRCONN) MAXMSGL(104857600) MCAUSER('MUSR_MQADMIN') replace

alter qmgr CHLAUTH(DISABLED)


### 리눅스 MQ 설치 커널값 

아래 설정 부탁드립니다.

 

1. SELinux 속성을 disabled 상태로 변경하여야 합니다. (시스템 재부팅 필요)

 

2. 커널파라미터 조정(최소값)

(v7.0)
kernel.msgmni = 1024
kernel.shmmni = 4096
kernel.shmall = 2097152
kernel.sem = 500 256000 250 1024
fs.file-max = 32768
net.ipv4.tcp_keepalive_time = 300

kernel.msgmni = 1024
kernel.shmmni = 4096
kernel.shmall = 2097152
kernel.shmmax = 268435456
kernel.sem = 500 256000 250 1024
fs.file-max = 524288


(v6.0)
  semmsl (sem:1)   = 128         
  semmns (sem:2)   = 16384       
  semopm (sem:3)   = 5           
  semmni (sem:4)   = 1024        
  shmmax           = 268435456   
  shmmni           = 1024        
  shmall           = 4194304     
  file-max         = 32768      
net.ipv4.tcp_keepalive_time = 300 


(v7.5)
kernel.shmmni = 4096
kernel.shmall = 2097152
kernel.shmmax = 268435456
kernel.sem = 500 256000 250 1024
fs.file-max = 524288


(v8.0)
kernel.shmmni = 4096
kernel.shmall = 2097152
kernel.shmmax = 268435456
kernel.sem = 32 4096 32 128
fs.file-max = 524288


(V9.2)
kernel.shmmni = 4096
kernel.shmall = 2097152
kernel.shmmax = 268435456
kernel.sem = 32 4096 32 128
fs.file-max = 524288

 
3. Maximum open files
mqm 유저에 대하여 open files 의 수를 최대로 조정
/proc/sys/fs/file-max


4. Process
mqm 유저에 대하여 최대 프로세스 및 스레드 수를 최대로 조정
linux PAM 을 사용하여, nproc 값을 4096 이상으로 조정

5. 유닉스 계열 공통사항으로 aix, hp, sun 과 동일한 부분

- 설치는 root 계정으로 진행됨

- 바이너리 영역 : /opt/mqm 1G 여유공간

- 데이터 영역 : /var/mqm 데이터 양에 따른 디스크 영역 할당 필요

- mqm 그룹 필요

- mqm 계정 필요

 
6. mqm 계정의 ulimit 값 설정

ulimit -n


### MQ 리스너 inetd
#WebSphere MQ
MQSeries1       1414/tcp        # IBM MQSeries
MQSeries2       1415/tcp        # IBM MQSeries
MQSeries3       1416/tcp        # IBM MQSeries
[mqm@WMQISVR1:/home/mqm] cat /etc/inetd.conf | grep MQ
####### IBM MQSeries
MQSeries1       stream  tcp     nowait  mqm     /opt/mqm/bin/amqcrsta   amqcrsta -m OZMQM1
MQSeries2       stream  tcp     nowait  mqm     /opt/mqm/bin/amqcrsta   amqcrsta -m OZMQML1
MQSeries3       stream  tcp     nowait  mqm     /opt/mqm/bin/amqcrsta   amqcrsta -m OZMQML2
[mqm@WMQISVR1:/home/mqm] 

등록하고 refresh 해야된다.


### 채널 트리거링
XQ에 initq 설정(SYSTEM.CHANNEL.INITQ) trigger GET(ENABLED) 프로세스 설정

DEFINE QLOCAL(TEST.XQ) TRIGGER INITQ(SYSTEM.CHANNEL.INITQ) PROCESS(TEST6.PROC) USAGE(XMITQ)
-- TRIGDATA(TEST6.TEST5)

채널도 설정

SYSTEM.CHANNEL.INITQ는 디폴트라 저절로 runmqchi 올라가있음 
근데 다른 IQ로 해서 할거면 채널시작기올려야함
START CHINIT INITQ(IQ)

프로세스 userdata 에 채널명을 넣어준다 

DEFINE PROCESS(TEST6.PROC) USERDATA(TEST6.TEST5)

### 큐 권한 설정
setmqaut -m BKQMGR -n "BKQMGR.DQ" -t q -g mqm -chg -clr -dlt -dsp -passall -passid -setall -setid -browse -get -inq -put -set

 -crt  없다 ?

setmqaut -m BKQMGR -n "BKQMGR.DQ" -t q -p min +all(추가)
setmqaut -m BKQMGR -n "BKQMGR.DQ" -t q -p min -all(빼기)

dspmqaut -m BKQMGR -n BKQMGR.DQ -t q -g mqm

dspmqaut로 하면 다보이는듯 ?? 정확하진않다

dmpmqaut로 해야 가지고 있는것만 보임


안심클릭서버의 JAVA API 어댑터는 root계정으로 실행하므로 root계정에 큐에 PUT, GET의 권한을 부여해야합니다.
PUT, GET 권한을 부여하는 방법은 다음과 같습니다.


setmqaut -m SFCAPP01 -t qmgr -p root +allmqi
setmqaut -m SFCAPP01 -n SYSTEM.CLUSTER.TRANSMIT.QUEUE -t q -p root +put
setmqaut -m SFCAPP01 -n MTE.EVQ -t q -p root +put
setmqaut -m SFCAPP01 -n QASR.JM.1.CQ -t q -p root +get
setmqaut -m SFCAPP02 -t qmgr -p root +allmqi
setmqaut -m SFCAPP02 -n SYSTEM.CLUSTER.TRANSMIT.QUEUE -t q -p root +put
setmqaut -m SFCAPP02 -n MTE.EVQ -t q -p root +put
setmqaut -m SFCAPP02 -n QASR.JM.1.CQ -t q -p root +get

### 큐매니져 자동 재시작
amqmdain auto QMGR

### 클러스터 가중치 설정

CLWLWGHT

The value must be in the range 1 through 99, where 1 is the lowest weighting and 99 is the highest.

This attribute is valid for channel types of:
 - Cluster sender
 - Cluster receiver


### 클러스터 재구성 스크립트

[대차중개 클러스터 제거]=========================================================

1. 큐매니저 suspend
suspend qmgr cluster(EAI.CLUS.01)
** DIS CLUSQMGR(*) SUSPEND 

2. 클러스터 큐 속성 제거
alt ql(AE.DE.S.REQ.LQ)  cluster('')
alt ql(DE.AE.S1.RLY.LQ) cluster('')
alt ql(DE.XX.S1.RLY.LQ) cluster('')
alt ql(DE.YY.S1.RLY.LQ) cluster('')
alt ql(XX.DE.S.REQ.LQ)  cluster('')
alt ql(YY.DE.S.REQ.LQ)  cluster('')
dis q(*) where(cluster eq 'EAI.CLUS.01')

3. 클러스터 수신 채널 속성 제거
alt chl(TO.DEAPP1) chltype(clusrcvr) cluster('')

4. 클러스터 수신 채널 정지
stop chl(TO.DEAPP1) MODE(FORCE) STATUS(INACTIVE)

5. 클러스터 수신 채널 삭제
delete chl(TO.DEAPP1)

6-1. 클러스터 송싱 채널 속성 제거 먼저
alt chl(TO.ITEAI1) chltype(clussdr) cluster('')

6-2. 클러스터 송신 채널 정지
stop chl(TO.ITEAI1) MODE(FORCE) STATUS(INACTIVE)
dis chs(to.iteai1) 

7. 클러스터 송신 채널 삭제
delete chl(TO.ITEAI1)

8. 클러스터 refresh
refresh cluster(EAI.CLUS.01) repos(yes)

클러스터 채널 정지되었는지 확인
dis chs(*)

resume qmgr cluster(EAI.CLUS.01)

 
[대차중개 클러스터 추가]=========================================================

 
1. 클러스터 수신 채널 생성
DEFINE CHANNEL('TO.DEAPP1') CHLTYPE(CLUSRCVR) CLUSTER('EAI.CLUS.01') CONNAME('PDEAPP1_EAI(1414)') MAXMSGL(104857600) 
DEFINE CHANNEL('TO.QM4') CHLTYPE(CLUSRCVR) CLUSTER('CLTEST') CONNAME('localhost(1818)') MAXMSGL(104857600) 

2. 클러스터 송신 채널 생성
DEFINE CHANNEL('TO.ITEAI1') CHLTYPE(CLUSSDR) CLUSTER('EAI.CLUS.01') CONNAME('PITEAI1_EAI(1414)') MAXMSGL(104857600)
DEFINE CHANNEL('TO.QM2') CHLTYPE(CLUSSDR) CLUSTER('CLTEST') CONNAME('localhost(1416)') MAXMSGL(104857600)

3. 클러스터 큐 속성 추가
alt ql(AE.DE.S.REQ.LQ)  cluster('EAI.CLUS.01')
alt ql(DE.AE.S1.RLY.LQ) cluster('EAI.CLUS.01')
alt ql(DE.XX.S1.RLY.LQ) cluster('EAI.CLUS.01')
alt ql(DE.YY.S1.RLY.LQ) cluster('EAI.CLUS.01')
alt ql(XX.DE.S.REQ.LQ)  cluster('EAI.CLUS.01')
alt ql(YY.DE.S.REQ.LQ)  cluster('EAI.CLUS.01')

dis q(*) where(cluster eq 'EAI.CLUS.01')

4. ITEAI1, ITEAI2 큐매니저에서 clusqmgr 확인
dis clusqmgr(*)

/usr/mqm/samp/bin/amqsput DE.AE.S1.RLY.LQ ITEAI1
/usr/mqm/samp/bin/amqsput DE.AE.S1.RLY.LQ ITEAI2


### MQ 트레이스 수집
간헐적으로 발생하는 문제에대해 다음과 같은 절차로 Wrapping Trace 를 받을 수 있습니다.
평소에 Trace On 상태를 유지하고, 특정 에러코드 또는 Probe ID 발생시 Trace Off 하여 문제시점의 Trace가 Overwrite 되는 것을 방지할 수 있습니다.
하지만 Disk I/O가 계속 발생하기에 운영시스템에서 적용여부는 고려되어야 합니다.

1. 먼저 /var/mqm/errors/*.LOG & /var/mqm/qmgrs/{Qmgr Name}/errors/*.LOG 파일을 삭제 또는 백업합니다.

2. 5MB(또는 적정치로..)의 크기로 Wrapping 되게 Trace를 시작합니다.
   (예로 5MB로 설정시, MQ 프로세스당 5MB의 TRC & TRS 파일이 rotation되기에 총 "10MB * 프로세스 수"의 Disk Space가 필요합니다.)
     strmqtrc -l 5 -t all -t detail -m {QMGR_NAME}

3. trapit 을 이용하여 원하는 에러코드(동양종금의 경우는 AMQ9416 또는 RM271000)가 발생하면 Trace를 종료하여 더이상 Overwrite 되는것을 방지합니다.
     trapit -i 3 -e RM271000 -f /var/mqm/qmgrs/\*.LOG -t 'endmqtrc -a'    
    (trapit 의 사용법은 스크립트를 열어보면 포함되어 있습니다.)

4. Trace 포맷팅 (under /var/mqm/trace directory)
    dspmqtrc *.TRC *.TRS

5. 수집정보
    /var/mqm/errors/*
    /var/mqm/qmgrs/<QMGR_NAME>/errors/*
    /var/mqm/trace/*.FMT

1. 먼저 /var/mqm/errors/*.LOG & /var/mqm/qmgrs/{Qmgr Name}/errors/*.LOG 파일을 삭제 또는 백업합니다.

2. 5M(또는 적정치로..)의 크기로 Wrapping 되게 Trace를 시작합니다
 strmqtrc -l 5 -t all -t detail -m <QMGR_NAME>
df -k 명령을 이용하여 디스크 full 이 발생하지 않는지 주기적으로 확인합니다.

3. 일정시간 후 Trace 를 종료합니다.
 endmqtrc -m <QMGR_NAME>

4. Trace 포맷팅 (under /var/mqm/trace directory)
dspmqtrc *.TRC *.TRS

5. 수집정보
/var/mqm/errors/*
/var/mqm/qmgrs/<QMGR_NAME>/errors/*
/var/mqm/trace/*.FMT

6. 트래이스 파일 삭제
rm -rf /var/mqm/trace/*TRC
rm -rf /var/mqm/trace/*TRS


### 트리거 종료
ps -ef | grep tmc | grep -v grep | awk '{print "kill -9",$2}' | sh -v >/dev/null 2>&1
ps -ef | grep tmc | grep -v grep | awk '{print $2}' | sh -v >/dev/null 2>&1


### 트리거 모니터 서비스 등록
DEFINE SERVICE ('TRM.SERVICE') +
DESCR(' ') +
STARTCMD('/opt/mqm/bin/runmqtrm') +
STARTARG('-m TEST -q TEST.INITQ') +
STOPCMD('/opt/mqm/bin/amqsstop') +
STOPARG('-m TEST -p +MQ_SERVER_PID+') +
STDOUT(' ') +
STDERR('/var/mqm/errors/trm.err.log') +
CONTROL(QMGR) +
SERVTYPE(SERVER) +
REPLACE

DEFINE SERVICE ('TRM.SERVICE') +
DESCR(' ') +
STARTCMD('/usr/mqm/bin/runmqtrm') +
STARTARG('-m MINTEST -q TEST.IQ') +
STOPCMD('/usr/mqm/bin/amqsstop') +
STOPARG('-m MINTEST -p +MQ_SERVER_PID+') +
STDOUT(' ') +
STDERR('/EAI/min/trm.err.log') +
CONTROL(QMGR) +
SERVTYPE(SERVER) +
REPLACE

DEFINE SERVICE ('INIT') +
DESCR(' ') +
STARTCMD('C:\Program Files\IBM\Websphere MQ\bin\runmqtrm') +
STARTARG('-m +QMNAME+ -q MY.INITQ') +
STOPCMD('C:\Program Files\IBM\Websphere MQ\bin\amqsstop') +
STOPARG('-m +QMNAME+ -p +MQ_SERVER_PID+') +
STDOUT('C:\Program Files\IBM\WebSphere MQ\errors\tm_out') +
STDERR('C:\Program Files\IBM\WebSphere MQ\errors\tm_err') +
CONTROL(QMGR) +
SERVTYPE(SERVER) +
REPLACE


### dmpmqmsg 사용법

-	백업 : dmpmqmsg –i{큐명} –f{파일명} –m{큐매니저명}

Ex) dmpmqmsg -iTEST.LQ -f./TEST.LQ.dat –mGSHUB


-	복원 : dmpmqmsg –o{큐명} –f{파일명} –m{큐매니저명}

Ex) dmpmqmsg –oTEST2.LQ -f./TEST.LQ.dat -mGSHUB



### 명령 서버 CONN 확인 명령어

dis conn(*) where (objname eq SYSTEM.ADMIN.COMMAND.QUEUE)

dis conn(*) where(pid  eq 53608604)



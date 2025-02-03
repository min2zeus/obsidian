---
sticker: emoji//1f64a
---
---
### AIX CMD로 계정만들기
mkuser pgrp='primary group' home='home folder path' account
 예)
mkuser pgrp='ssaweb' home='/ssaweb/andon' andon

### AIX 메모리 사용률
http://mwguru.tistory.com/32
http://cafe.naver.com/iamstrong/2473
svmon -O summary=basic,unit=auto
svmon -Pu -t 20 | grep -p Pid | grep '^.*[0-9]' | grep mte
svmon -P $PID -m -r 
svmon -P 24772784 | grep mte
svmon -P 10027250 | grep mte
nohup svmon -P 10027250  -m -r -i 1800 >> output1.file &
nohup svmon -P 24772784  -m -r -i 1800 >> output2.file &
nohup svmon -P 12779710  -m -r -i 1800 >> output3.file &
nohup svmon -P 20643900  -m -r -i 1800 >> output4.file &
svmon -P 4980958 | grep mte
svmon -P 13041888 | grep mte
svmon -P 18153718 | grep mte
svmon -P 18415620 | grep mte

### inetd 재시작
-- AIX - refresh -s inetd  
AIX - refresh -s syslogd
HP - inetd -c
Sun - pkill -HUP inetd (pid)


### OS 별 LIBPATH
AIX            LIBPATH, LD_LIBRARY_PATH 
OS/2           LIBPATH
Windows NT/95  PATH
Solaris/Linux     LD_LIBRARY_PATH
HP/UX          SHLIB_PATH, LD_LIBRARY_PATH (64bit only)



AIX - export LIBPATH=./lib:$LIBPATH   
HP - export SHLIB_PATH=./lib:$SHLIB_PATH
Sun - export LD_LIBRARY_PATH=./lib:$LD_LIBRARY_PATH



SUN일 경우 LD_LIBRARY_PATH를
HP-UX일 경우 SHLIB_PATH를,
AIX일 경우 LIBPATH를 사용합니다. 

참고 : HP에서 LD_LIBRARY_PATH 는 64비트 환경에서만 작동함. 확장자 명도 sl 임


### OS 별 메모리 확인

AIX - topas , nmon   
HP - top, top -s [time]
Sun - prstat -s [key]
      key = cpu, time, size ,rss, pri
     
      vmstat -S 3    -- 잘모르겠다.


ps cu | head -n 1;
ps cu | egrep -v "CPU|kproc" | sort +3b -n -r | grep mqm | head -n 3 


### OS 별 시스템 확인
@@ Solaris

1. 물리적 CPU 수
 psrinfo -p
2. 물리적 core 수
  kstat cpu_info | grep core_id | uniq | wc -l
3. 물리적 CPU 수 및 각 CPU 당 Virtual Processor 수(시스템 모델도 나옴)
 psrinfo -pv


@@ AIX

prtconf
prtconf lsdev -Cc processor
이거면 다나옴

시스템모델(uname -M), CPU type, Core 수

패치는 oslevel -r
@@ HP-UX (ia64만 machinfo 됨)

ioscan -fknC processor
ioscan -fkC processor

/usr/contrib/bin/machinfo

HP 커널값 확인
/usr/sbin/kmtune

### scp 파일 보내기 

scp mte*2012.tar mqm@165.141.112.81:/tmp
scp  파일이름  아이디@아이피:/디렉토리


### 프롬프트에 계정, path 나오게 하는 방법
.profile에 추가
export PS1='['`hostname`':$LOGNAME:$PWD] '
export PS1='[$LOGNAME@'`hostname`':$PWD] '


### ^M 지우는 방법
1,$s/^M//g
^M은 ctrl + v +M
여러개의 파일들을 한꺼번에 변환할때
>find . -type f -exec perl -pi -e 's/^M//g' {} \;

### zmoderm 다운
yum -y install lrzsz
드래그 

rz
sz 다운

ma2p2@mocomsys.com

### 리눅스 CPU / MEM

CPU
grep ^processor /proc/cpuinfo | wc -l

메모리
cat /proc/meminfo | grep MemTotal


### 리눅스 패키지 다운
sudo apt-get install ksh
yum install abcd

### 리눅스 방화벽 상태 / 정지 확인

 service iptables save
 service iptables stop
 chkconfig iptables off
 service iptables status ( 확인)


### 메모리 확인 쉘

메모리 확인 쉘

ps -ef | grep MQMSGGET
현재 구동중인 모든 MQMSGGET의 PID를 확인합니다
nohup sh mem.sh $PID & 
ex : nohup sh mem.sh 1425 &
위의 과정을 반복하여 PID의 수 만큼 쉘을 띄웁니다.
쉘 실행 위치에 $PID.mem.log 파일이 생성됩니다. 
ex : 1425.mem.log
루프 주기는 10분으로 지정하였습니다.

while true                                                                                        
do                                                                                                
ret=`ps -elf | grep "$1" | grep -v grep | grep -v mem.sh | grep -v mem.log | awk '{print $10}'`   
if test -z "$ret"; then                                                                           
	ret='Process Not Found'                                                                         
fi                                                                                                
echo [`date`] SZ of $1 : $ret >> $1.mem.log                                                       
sleep 600                                                                                         
done                                                                                              


### 원격데스크탑 (윈도우 재시작)
shutdown -r -t 0


### AIX 메모리
mqm@aix_71>lscfg -vp | grep WAY
      08-WAY PROC CUOD:   
	  8코어 칩 1개
[aix_61:mqm@/home/mqm]lparstat -i | grep Physical
Maximum Physical CPUs in system            : 8
Active Physical CPUs in system             : 8
Shared Physical CPUs in system             : 0
Physical CPU Percentage                    : 100.00%
Physical Memory in the Pool                : -

### awk 사용 방식
dspmq | awk -F\( '{print $2}' | awk -F\) '{print $1}'
dspmq | awk -FQ '{print $2}'

cat test.txt | awk -F\[ '{print $3}' 

cat test.txt | awk -F\[ '{print $4}' | awk -F\] '{print $1}'
cat test.txt | awk -F\[ '{print $5}' | awk -F\] '{print $1}'
cat test.txt | awk -F\[ '{print $6}' | awk -F\] '{print $1}'

cat test.txt | awk -F\[ '{print $6}' | awk -F\] '{print $1}' | awk -F\( '{print $1}'

### DATE 시간찍기

date '+%Y/%m/%d %H:%M:%S' 

### replace 스크립트

FILEs=`find . -name "*.xml"`
for fname in $FILEs
do
mv $fname $fname".org"
cat $fname".org" | sed -e 's/password="test"/password="test1"/g' > $fname
rm -if $fname".org"
done

#FILEs=`find . -name "*.xml"`
FILEs=`find . -name "db2send.xml"`
for fname in $FILEs
do
cp $fname $fname".bak"
#mv $fname $fname".org"

cat $fname | sed -e 's/record_name="record" use_cursor="0"/record_name="record123123" use_cursor="0"/g' > $fname.org
cat $fname".org" | sed -e 's/a=jaeewon/a=minji/g' > $fname
cat $fname | sed -e 's/user="test" password="test01920" conn="orcl"/user="test" password="test" conn="orcl"/g' > $fname.org

mv $fname".org" $fname
rm -if $fname".org"
done


### 특정 문자 포함된 내용 찾기 (xargs)
find ./ -name "*" |xargs grep "4416"


###  EAI connection 에러
AIX 7에서 OS LEVEL tcptr_enable 값에 따라 실행 여부가 결정됩니다. (aix 7.1 패치버전에서 OS 방화벽 정책이 적용되어서(디폴트는 미적용임))
tcptr_enable = 1 자체 방화벽 기능으로 포트 제한하게 됨
tcptr_enable = 0 방화벽 해제
root 권한에서 tcptr ?show 하면 상세 내역 확인 가능
-관련 Parameter : tcptr_enable=1 (방화벽 적용)
                         ==> '0' 으로 수정(방화벽 미 적용)

-tcptr_enable : aix 6.1 부터 나온 port별 tcp incomming connection 갯수 제한
                     default=0으로서 정책이 적용안되는 것이 정상임.
                     ==> 어떤 사유에 의해 적용 되었는지 확인 안됨.(이지에 확인 요청 상태임) -> 신상품 프로젝트 다른 보안 솔루션
					 

					 
### TCP 소켓 에러 코드

AIX : /usr/include/errno.h
HP : /usr/include/sys/errno.h
Solaris : /usr/include/sys/errno.h

Windows : MS site
http://msdn.microsoft.com/en-us/library/windows/desktop/ms740668(v=vs.85).aspx



### 쉘안에서 기동되는 java 에 입력값 적용
(echo localhost; echo 7474; echo SYSTEM.DEF.SVRCONN; echo mqm; echo ''; echo MQ92;) | java MQIVP


- for 문 추가
for i in 1 2 3
	do
        (echo localhost; echo 7474; echo SYSTEM.DEF.SVRCONN; echo mqm; echo ''; echo MQ92;) | java MQIVP
        sleep 1
	done;
mqm@kbmq1:~/min:> 


### Cisco 계정

| ID    | PASS  | 사용자     |
| ----- | ----- | ------- |
| ma104 | ma14$ | 홍종민     |
|       |       | 이민지     |
|       |       | 임준수     |
| ma105 | ma15$ | 공정환     |
|       |       | 국종우<br> |
| ma106 | ma16$ |         |
| ma107 | ma17$ |         |
| ma108 | ma18$ |         |
| ma109 | ma19$ |         |

### Defect 계정
lmjalswl
1234qwer!

### perl 사용
디폴트 큐매니져 변경
perl -pi -e 's/ESB1D/ESB1P/g' /var/mqsi/components/ESB1PBK/servers/ESB*/over*/server*

/ 내용 바꾸는 법 \\ 사용
perl -pi -e 's/opt'\/\mqm/test/g' start.sh


### nmon_analyser 사용

nmon -f -t -s 1 -c 10
	    1초  10번
nmon 파일 출력

sort AIX72_241122_1652.nmon > AIX72_241122_1652.csv

파일 csv 파일로 변경
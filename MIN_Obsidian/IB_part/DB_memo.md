---
sticker: emoji//2620-fe0f
---
### DB2 구성 오라클

오라클계정으로 오라클 띄어야 함
mqm 계정으로 해서
ORA-01552: cannot use system rollback segment for non-system tablespace 'USERS'
이거나와도  select segment_name, tablespace_name, status from dba_rollback_segs; 이거로보면 USER 안보임
(alter rollback segment USERS online)
mqm 계정으로 올린 ora 프로세스 다 종료 후 Oracle 계정으로  


db2 get dbm cfg   | grep SVCENAME
db2 update dbm cfg using svcename 50005
db2stop force
db2start

### DB2 설치
/tmp/db2_install.log.9437998

A minor error occurred while installing "DB2 Enterprise Server Edition" on 
this computer. Some features may not function correctly.

For more information see the DB2 installation log at 
"/tmp/db2_install.log.9437998".

설치는 db2_install 이고
패치는 installFixPack 이걸로 해야함 (패치에 있는 db2_install로 하면 한번에 다 된다함)

셋업파일 위치
/home/mqm/SKB/db2_95/setup/db2/license
/opt/IBM/db2/V9.1/adm/db2licm -a db2ese.lic
/opt/IBM/db2/V9.5/adm/db2licm -a db2ese.lic

위치
/opt/IBM/db2/V9.5/install

./db2ls -q -a

A minor error occurred while installing "DB2 Enterprise Server Edition" on 
this computer. Some features may not function correctly.

For more information see the DB2 installation log at 
"/tmp/db2_install.log.34734554".

### DB2 원격인스턴스 등록 및 접속

1. 원격 인스턴스 등록
db2 catalog tcpip node <노드명> remote <서버ip> server <포트>
 
2. 원격 DB 등록
db2 catalog db <DB 이름> as <별칭> at node <노드명>
 
3. 원격 DB 접속
db2 connect to <별칭> user <ID> using <PASSWORD>
 
4. 원격 인스턴스 정보 수정
db2 uncatalog node <노드명>
db2 catalog tcpip node <노드명> remote <서버ip> server <포트>

===================================================================================

db2 catalog tcpip node NEAI remote 10.10.11.50 server 50005
db2 catalog DB EAIDB as AEAI at node NEAI
db2 connect to AEAI user db2inst1 using db2inst1

### DB2 테이블스페이스생성

** table space 만들기

db2 connect to IFM
db2
create bufferpool bp4k size 12800 pagesize 4k
create bufferpool bp32k size 12800 pagesize 32k
create tablespace DTT_DS01 pagesize 32k managed by database using(file'/home/db2inst5/db2inst5/NODE0000/IFM/DTT_DS01.dat' 200 m) autoresize yes increasesize 20 m maxsize 400 m bufferpool bp32k
create temporary tablespace temp32k pagesize 32k managed by system using('/home/db2inst5/db2inst5/NODE0000/IFM/TEMP32K') bufferpool bp32k
create tablespace DTT_IX01 pagesize 4k managed by database using(file '/home/db2inst5/db2inst5/NODE0000/IFM/testspaceidx.dat' 300m) autoresize yes increasesize 300 m maxsize 500 m bufferpool bp4k

* 권한주기
grant dbadm on database to IFM
grant use of tablespace DTT_DS01 to user mqsi6
grant use of tablespace DTT_IX01 to user mqsi6

### 오라클 ORA-25408 에러 
제    품   TUXEDO
사용  OS   모든 플랫폼(platform)

이슈사항   Oracle session failover(cf: BEST 2003년 여름호, Oracle - 3113 에러 시 자동 재접속 방법)
           후에 대부분의 작업에 문제가 없으나 일부 application의 경우 xa_close 또는 xa_commit시에
           XAER_RMERR 에러가 발생하면서 xa trace 파일에는 관련하여 다음과 같은 메시지가 발생한다.
           ===
           oerr ora 25408
           25408, 00000, "can not safely replay call"
           // *Cause: The connection was lost while doing this call. It may not be
           // safe to replay it after failover.
           // *Action: Check to see if the results of the call have taken place, and then
           // replay it if desired
===
해 결 안   Oracle 서버의 failover후에 session이 재생성되는 시점은 xa_start시(xa_start시 session이 끊겨 있다
           고 판단되는 경우, xa_open작업을 시도함)이다. 따라서 xa_start 작업이 없었던 application의 경우
           session이 재생성 되어있지 않기 때문에 xa 작업 요청시 위와 같은 에러가 발생하게 된다.

		   
### 오라클 ORA-28000 에러

1) USER 패스워드 만료 상태 확인하기

1. system계정으로 로그인 하여 다음을 입력 한다.
 C:\> sqlplus "/as sysdba" --system계정으로 로그인
...
SQL> select * from dba_users; --DB유저 정보 확인하기

2. ACCOUNT_STATUS컬럼을 확인한다.
- OPEN : 정상
- LOCKED(TIMED) : 패스워드 설정 횟수 이상 잘못입력하여 잠김
- EXPIRED & LOCKED : 패스워드 기간이 만료되어 잠김
...
접속 시 오류난 유저의 ACCOUNT_STATUS컬럼을 보면 LOCKED(TIMED)으로 되어 있을 것 이다.


2) LOCK걸린 유저 UNLOCK하기

1. system계정으로 로그인 하여 다음을 입력 한다.
C:\> sqlplus "/as sysdab" --system계정으로 로그인
...
SQL> alter user 유저명 account unlock; --LOCK걸린 유저 UNLOCK하기

2. LOCK되었던 유저로 로그인을 확인한다.



### TNS 리스너 없습니다
lsnrctl
status
reload   리스너.ora 바꿨으면 reload 해주라
stop
start
status

### 사내 DB 정보

DB	IP	Port	SID	DB명	ID	Password		JDBC Url						Driver
Oracle	10.10.1.170	1521	ORCL	ORCL	test	test		jdbc:oracle:thin:@10.10.1.170:1521:ORCL			oracle.jdbc.OracleDriver
DB2	10.10.1.170	50000	sample	sample	test	test		jdbc:db2://10.10.1.170:50000/sample			com.ibm.db2.jcc.DB2Driver
Tibero	10.10.1.170	8629	tibero	tibero	test	test		jdbc:tibero:thin:@10.10.1.170:8629:tibero		com.tmax.tibero.jdbc.TbDriver
Altibase10.10.1.170	20300	mydb	mydb	test	test		jdbc:Altibase://10.10.1.170:20300/mydb			Altibase.jdbc.driver.AltibaseDriver
MariaDB	10.10.1.170	3306	test	test	test	test		jdbc:mysql://10.10.1.170:3306/test			com.mysql.jdbc.Driver
MSSQL	10.10.1.170	1433	testdb	testdb	SA	mocomsys1$	jdbc:sqlserver://10.10.1.170:1433;DatabaseName=testdb	com.microsoft.sqlserver.jdbc.SQLServerDriver


### 오라클 계정생성 및 권한 설정
conn sys/oracle as sysdba

drop user min cascade;

create user MIN 	identified by MIN;
create user mintest	identified by mintest;

alter user min identified by min2;

grant connect, resource, dba to MIN;

conn MIN/MIN
select * from tabs;

create table test(id varchar2(10), name varchar2(10), flag varchar2(1));

sqlplus 설정(해당 명령은 오라클10g 서버 혹은 클라이언트가 설치 되어있어야 함.)
 

 
1. cmd 창 실행
 
2.sqlplus 아이디/패스워드@호스트 문자열
 (sqlplus system/manager 기본접속 정보?)
(만일 호스트 문자열이 설정되어있지 않다면.

oracle설치 폴더\product\10.2.0\client_1\network\ADMIN\SAMPLE 폴더로 이동


 sqlnet.ora 파일 편집 

SQLNET.AUTHENTICATION_SERVICES= (NTS)
 NAMES.DIRECTORY_PATH= (TNSNAMES, ONAMES,HOSTNAME)
 

 
tnsnames.ora 파일 편집
 (?ORCL <== 호스트 문자열
 HOST <==원격 요청지 IP
PORT <== 설정 포트번호)
 
 
?ORCL =
  (DESCRIPTION =
    (ADDRESS_LIST =
      (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.10.103)(PORT = 1521))
    )
    (CONNECT_DATA =
      (SERVICE_NAME = ORCL)
    )
  )
 

3. [ 테이블스페이스 생성 ]
 
create tablespace 테이블스페이스명
datafile '/DATA/oradate/테이블스페이스명.dbf'
size 1024M ;
'/DATA/oradate/테이블스페이스명.dbf' 이부분에 그냥 테이블스페이스명.dbf 만 설정하는 경우도 있음.
 
 
4. [ 사용자 생성 ]
 
create user 유저명
identified by 비밀번호
 
default tablespace 테이블스페이스명;

5. [ 생성 사용자에게 권한 주기 ]
grant resource,connect to 유저명;
grant dba to 유저명; (위 옵션에 추가해서 함께 설정 가능함)

6. [ 테이블스페이스 사이즈증가 ]
 ALTER TABLESPACE TISPMGT_TS ADD DATAFILE 
'/DATA/oradata/테이블스페이스명.dbf' SIZE 10G AUTOEXTEND ON NEXT 20M MAXSIZE 10G
 
7. [ 테이블스페이스 데이터파일 증가 ]
 
ALTER DATABASE DATAFILE '/DATA/oradata/테이블스페이스명.dbf' 
resize 10G
 
8. [ 테이블스페이스 데이터파일 자동증가 ]
ALTER DATABASE DATAFILE '/DATA/oradata/테이블스페이스명.dbf' 
autoextends on next 10G max size 20G
 
dbms_lock package 사용하기위한 권한 설정

grant execute on dbms_lock to public;

### 오라클 인스턴트 사용
http://www.oracle.com/technetwork/indexes/downloads/index.html

오라클 인스턴트로 사용할 때

오라클 홈페이지에서 관련 설치 후 sqlplus 도 설치하고 

같은 디렉토리에 푼다음

export ORACLE_HOME=/home/oracle/setup/instant/instantclient_11_2
export LIBPATH=/home/oracle/setup/instant/instantclient_11_2:$LIBPATH
export TNS_ADMIN=/home/oracle/setup/instant/instantclient_11_2

설정 후 맞는 tnsnames.ora 입력

export PATH=/home/mqm/min/bin:$PATH
export EAI_HOME=/home/mqm/min
export LD_LIBRARY_PATH=/opt/mqm/lib64:$LD_LIBRARY_PATH 



export ORACLE_HOME=/home/mqm/min/DB2/instantclient_11_2
export LD_LIBRARY_PATH=/home/mqm/min/DB2/instantclient_11_2:$LD_LIBRARY_PATH
export TNS_ADMIN=/home/mqm/min/DB2/instantclient_11_2

### 오라클 컬럼 정렬
set linesize 170
col parameter format a40
col value format a50


### 오라클 지원 여부
오라클 10은 오라클 8 release3 부터 지원
오라클 11은 오라클 9 release2 부터 지원




### 오라클 프로시저
오라클 sql developer 에서 프로시져 실행하면 
anonymous block completed 이렇게만 보일때

set serveroutput on format wraped;

하면 프로시져 실행결과도 보임

create or replace PROCEDURE P_ELAPSED_TEST1
IS
 v_time1    DATE;
 v_time2    DATE;
 v_Elapsed            NUMBER;
 
BEGIN
 select sysdate into v_time1 from dual;
 -- dbms_lock.sleep() specifies the sleep time in seconds --
 dbms_lock.sleep(3);
 select sysdate into v_time2 from dual;
 -- You can subtract two DATE values, and the result is a FLOAT which is the number of days between the two DATE values.  --
 -- ref : http://www-db.stanford.edu/~ullman/fcdb/oracle/or-time.html#operations            --
 v_Elapsed := (v_time2-v_time1) * 86400; 
 -- print --
 dbms_output.enable(20000);
 dbms_output.put_line('start time : '||to_char(v_time1,'yy/mm/dd hh24:mi:ss'));
 dbms_output.put_line('end time : '||to_char(v_time2,'yy/mm/dd hh24:mi:ss'));
 dbms_output.put_line('elapsed time : '||v_Elapsed );
END P_ELAPSED_TEST1;


create or replace
PROCEDURE P_INSERT
(iID IN mintest.ID%TYPE, iNAME IN mintest.NAME%TYPE, iAGE IN mintest.AGE%TYPE, iADDR IN mintest.ADDR%TYPE)
IS
BEGIN
  insert into mintest values(iID, iNAME, iAGE, iADDR);
END;

exec P_INSERT('No.1', 'MIN', 20, 'Seoul');


create or replace PROCEDURE P_TEST(ADDR IN VARCHAR2)
IS
  NAME VARCHAR2(10) :='HONG';
BEGIN
  dbms_output.put_line('이름 : ' || NAME);
  dbms_output.put_line('주소: ' || ADDR);
END;


exec P_TEST('Jeju');

create or replace PROCEDURE P_TTTEST
IS
BEGIN
dbms_output.put_line('==========dd');
EXCEPTION WHEN OTHERS THEN
       dbms_output.put_line('ERRORS');
end;

### 외부 툴에서 DB2 접속방법

-- 인스턴스 접속
db2 attach to db2inst1

-- 설정값 보기
db2 get dbm cfg show detail


--SVCENAME 에 port 넘버 아니면 /etc/services 에 설정된 서비스명을 써주면 토드 등 외부 툴에서 붙을 수 있다.
db2 update dbm cfg using SVCENAME DB2_mte
db2stop
db2start?


-- netstat로 확인
netstat -an | grep 60000


-- 이게 되어있어야 함
db2set DB2COMM=tcpip


### DB2 테이블 스페이스 계정 생성

CREATE TABLESPACE TEST DATAFILE 'C:\TEST.DBF' SIZE 100M;
CREATE USER MIN IDENTIFIED BY MIN DEFAULT TABLESPACE TEST TEMPORARY TABLESPACE TEMP;
GRANT CONNECT, RESOURCE TO MIN;

CREATE TABLESPACE TEST DATAFILE 'D:\ProgramFiles2\oracle\product\10.2.0\MTE\TEST.DBF' SIZE 100M;
CREATE USER BYP IDENTIFIED BY BYP DEFAULT TABLESPACE TEST TEMPORARY TABLESPACE TEMP;
GRANT CONNECT, RESOURCE TO BYP;

CREATE TABLESPACE MIN DATAFILE 'C:\DB2\NODE0000\MTEIDT\T0000005\MIN.DBF' SIZE 10M;
CREATE USER db2admin IDENTIFIED BY mocomsys DEFAULT TABLESPACE MIN TEMPORARY TABLESPACE TEMP;
GRANT CONNECT, RESOURCE TO db2admin;

db2 "create tablespace MIN pagesize 32k managed by database using(file 'C:\DB2\NODE0000\MTEIDT\T0000005\MIN.DAT' 1 g) autoresize yes increasesize 1 g maxsize 5 g bufferpool bp32k"
db2 "create bufferpool bp32k size 12800 pagesize 32k"
db2 "select * from syscat.bufferpools"
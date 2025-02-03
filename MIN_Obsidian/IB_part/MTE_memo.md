---
sticker: emoji//1f916
---
---
### Adapter DB put 에러
Adapter에서 수신쪽에서 processed 뜨면서 정상처리로 보이는데 
DB에 data가 쌓이지 않으면 
송신과 수신 xml 에서 data_name 과 record_name 이 일치하는 지 확인

### Adapter xml 프로시져
begin end 로 해결 	

begin
 프로시저();
end;

<SQL>
BEGIN
update_sal(1);
END;
</SQL>

### fsend 송신 쉘 (윈도우 파일 송신 쉘)
@echo off
echo "MEFA START : %DATE% %TIME%"
set EAI_HOME=C:\Adapter

REM "=========================================================================="
REM 환경 변수 설정.
REM "=========================================================================="
REM 정상처리(DTT에서 폴더 구분자 보이지 않는 문제는 있음)

SET EAI_SEND_DIR=C:\Users\Min\Desktop\SEND
SET EAI_BKUP_DIR=C:\Users\Min\Desktop\Backup
SET EAI_RECV_DIR=Z:\temp
REM \\LJW-X230\Users\Jaewon\Desktop

REM "=========================================================================="
REM EXECUTE
REM "=========================================================================="

CD %EAI_SEND_DIR%
 
FOR %%C IN (*.*) DO CALL :WORK %%C
echo "MEFA END: %DATE% %TIME%"
goto :END

:WORK
echo EAI_SEND_FILE=%1
echo "---------------------------"
"%EAI_HOME%\bin\mefa" --lock_disable --adapter.in.file.filename=%EAI_SEND_DIR%\%1 --adapter.in.file.rename_dir=%EAI_BKUP_DIR% --replace "#RECV_TMP_DIR#=%EAI_RECV_DIR%" --replace "#RECV_TMP_FILE#=%1" --replace "#BKUP_TMP_DIR#=%EAI_BKUP_DIR%" --replace "#BKUP_TMP_FILE#=%1" "C:\Adapter\conf\fsend.xml"
echo "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

:END

### INDOUBTERR 파일 (MTE Agent)
INDOUBTERR-xxxxx   파일
장애가났을때 메세지가 유실되었는지 판별하기위해서 
롤백커밋 안되었을때 

### MTE requset/apply Adapter xml

SQL을 PLSQL을 쓰면 

deferred_define="0" no_fetch="0"   이부분을 1로 설정
deferred_define="1" no_fetch="1" 

req_format 도 설정하는 포맷으로 맞춰준다

그리고 separator로 할 시 
req_record_separator="w^h^" req_field_separator="w^"
이런식으로 앞에 req 붙여 준다.

fixed_length 는
<field key="0" nofetch="0" name="SUBJ_NO" pass="in" req_start="1" req_length="16"/>
이런식으로 field 에서 start랑 length 맞춰준다. 

그리고 
preaction
postaction
sql 에서 (update) 생략

sql에 select 문 

pre 나 post 는 한트랜잭션 밖에 (한 로우) 만 처리할 수 있어서 pre 나 post 에는 
insert delete update 그런거 넣어준다 상황봐서 

sql 에 select 넣어주고 
update는 여러 로우라고 해도 한 트랜잭션으로 인식이 되는거다.
우선 select 는 sql에 넣어주자 다른걸 pre 나 post로 빼고

### File Adapter 송신에서 경로 설정
- 송신 실행
mefa --adapter.in.filename="절대경로송신파일명" --replace "#RECV_DIR#=수신디렉토리명" --replace "#RECV_FILE#=수신파일명" send.xml
ex) 
mefa --adapter.in.filename="/home/mte/mte_adapter/bin/file_test/SNDR/data.txt" --replace "#RECV_DIR#=/home/mte/mte_adapter/bin/file_test/RCVR" --replace "#RECV_FILE#=data.txt" send.xml

vi sndr.xml
RFH2 헤더부분을 수정
<directroy>#RECV_DIR#</directroy>
<file_name>#RECV_FILE#</file_name>

환경변수 할시
export FILENAME=file.txt
--replace "#SND_FILE#=$FILENAME"

쉘 파라미터 첫번째
s.sh file.txt
--replace "#SND_FILE#=$1"

### MTE PowerPack 메시지 백업 차이
모든내용 : ZIP 파일로 바로 복원 가능(MQMD 포함)

아래 두개는 MQMD가 포함되지 않음
Header+data : RFH2 + data 백업하며, .dat 파일로 만들어져서 생성기로 복원 필요
data :  data 백업하며, .dat 파일로 만들어져서 생성기로 복원 필요

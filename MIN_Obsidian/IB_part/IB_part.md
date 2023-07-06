


```dataview
TABLE
	dateformat(file.cday, "yyyy-MM-dd (cccc)") as 작성일,
	dateformat(file.mday, "yyyy-MM-dd (cccc)") as 수정일
FROM "IB_part"
WHERE file.cday > date(2023-07-05)
SORT 작성일 desc
```



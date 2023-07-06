---
sticker: emoji//1f468-200d-1f4bc
---
---
[MQ::[[MQ_memo]]]
[IIB::[[IIB_memo]]]
[MFT::[[MFT_memo]]]
[MTE::[[MTE_memo]]]
[ETC::[[ETC_memo(OS, vi, network)]]]

---

```dataview
TABLE 
	column as "MQ",
	column as "IIB",
	column as "MFT",
	column as "MTE",
	column as "ETC"
FROM "MIN_Obsidian/frontmatter"
where file.cday > date(2023-07-06)
sort file.name ASC
```


```dataview
TABLE time-played, length, rating
FROM "Test" SORT rating desc
```

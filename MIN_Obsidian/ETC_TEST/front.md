---
created: 2023-07-04
wakeup: 07:00
sleep: 23:30
workout: ✅
gratitude: ✅
제목: 일기
---
# 2021-11-12 Daily Notes

```dataview
TABLE without ID
제목 as title,
wakeup as 기상시간, sleep as 취침시간, workout as 운동, gratitude as 감사일기,
dateformat(created, "yyyy-MM-dd (cccc)") as 작성일
FROM "front"
```

## BoostCamp 4주차과제
#### 17/1/23
##### done
- ButterKnife이용, Realm이용,
- Realm insert 및 findAll 사용,
- AlarmDetailActivity activity 생성
- TimePicker 생성
- RecyclerView구현했으나 디자인 매우이상함

##### todo
- 기존 데이터 클릭시 수정화면으로 진입.(AlarmDetailActivity수정해서 사용)
- RecyclerView item에서 시계모양 클릭시 데이터베이스 조정하기
- 알람 생성시 AlarmManager 등록하기
- BroadCastListener만들기
- BroadCastListener에서 notification 생성하기
- RecyclerView item에서 시계모양 클릭시 AlarmManager 조정하기

##### fixlist
- 요일 선택 잘못만듬 수정해야함
- RecyclerViewItem 디자인수정해야함

#### 17/1/24
- 하루종일 일있어서 제대로 못함ㅜㅜ

#### 17/1/25

##### done
- ButterKnife이용, Realm이용,
- Realm insert 및 findAll 사용,
- AlarmDetailActivity activity 생성
- TimePicker 생성
- RecyclerView구현했으나 디자인 매우이상함
- 기존 데이터 클릭시 수정화면으로 진입.(AlarmDetailActivity수정해서 사용)
- 수정화면에서 수정후 데이터 저장
- 요일 선택 완료
- RecyclerView item에서 시계모양 클릭시 데이터베이스 조정하기
- BroadCastListener만들기
- BroadCastListener에서 service 호출하기
- Service에서 Notification 생성 후 등록하기
- RecyclerViewItem 디자인수정해야함
- ForegroundService로 실행
- Notification에서 바로 끌 수 있는 Action 등록
- Service에서 음악 재생 및 종료 만듬
- 알람끄는 페이지 생성

##### todo

##### fixlist

#### 17/1/26


##### done
- ButterKnife이용, Realm이용,
- Realm insert 및 findAll 사용,
- AlarmDetailActivity activity 생성
- TimePicker 생성
- RecyclerView구현했으나 디자인 매우이상함
- 기존 데이터 클릭시 수정화면으로 진입.(AlarmDetailActivity수정해서 사용)
- 수정화면에서 수정후 데이터 저장
- 요일 선택 완료
- RecyclerView item에서 시계모양 클릭시 데이터베이스 조정하기
- BroadCastListener만들기
- BroadCastListener에서 service 호출하기
- Service에서 Notification 생성 후 등록하기
- RecyclerViewItem 디자인수정해야함
- ForegroundService로 실행
- Notification에서 바로 끌 수 있는 Action 등록
- Service에서 음악 재생 및 종료 만듬
- 알람끄는 페이지 생성

- RecyclerView item에서 시계모양 클릭시 AlarmManager 조정하기
- 알람 repeat 속성 등록하기
- 알람 요일에 따라 그만큼 만들고 등록하기 (기존 id * 10 + 0~6(요일에따라) 로 PendingIntent의 code를 정해주자)
- 알람 취소 생성(요일별 정보를 들고 있다가 for를 돌면서 id * 10 + 0~6를 만들어서 취소시키기)
- 알람 재거시 등록취소 확인하기

##### todo
- __ 음악이 꺼져있으면 진동울리는 방법 구상 __
- ContentProvider 생성
- 재부팅시 재등록 로직 만들기
- 알람 울릴시 위치 or 날시정보가져오게 만들기
- '반복' '만들기
- 저장한 메모 가져오기
##### fixlist

#### 17/1/27
##### done
- ButterKnife이용, Realm이용,
- Realm insert 및 findAll 사용,
- AlarmDetailActivity activity 생성
- TimePicker 생성
- RecyclerView구현했으나 디자인 매우이상함
- 기존 데이터 클릭시 수정화면으로 진입.(AlarmDetailActivity수정해서 사용)
- 수정화면에서 수정후 데이터 저장
- 요일 선택 완료
- RecyclerView item에서 시계모양 클릭시 데이터베이스 조정하기
- BroadCastListener만들기
- BroadCastListener에서 service 호출하기
- Service에서 Notification 생성 후 등록하기
- RecyclerViewItem 디자인수정해야함
- ForegroundService로 실행
- Notification에서 바로 끌 수 있는 Action 등록
- Service에서 음악 재생 및 종료 만듬
- 알람끄는 페이지 생성
- RecyclerView item에서 시계모양 클릭시 AlarmManager 조정하기
- 알람 repeat 속성 등록하기
- 알람 요일에 따라 그만큼 만들고 등록하기 (기존 id * 10 + 0~6(요일에따라) 로 PendingIntent의 code를 정해주자)
- 알람 취소 생성(요일별 정보를 들고 있다가 for를 돌면서 id * 10 + 0~6를 만들어서 취소시키기)
- 알람 재거시 등록취소 확인하기

- ContentProvider 생성
- alarm등록 static으로 만들어서 여기저기서 사용 할 수 있게 만들고 ContentProvider에 업데이트하기
- 저장한 메모 가져오기
- 반복로직만들기
- 재부팅시 재등록 로직 만들기
- 진동구현
- 알람 소리 적용
##### todo
- 알람 울릴시 위치 or 날시정보가져오게 만들기
- provider 확인
- 알람 울리고 나면 반복 설정 안해 둔 경우 알람 끄기<- 아니면 재부팅 할 때마다 다시됨.

#### 17/1/28

##### done
- ButterKnife이용, Realm이용,
- Realm insert 및 findAll 사용,
- AlarmDetailActivity activity 생성
- TimePicker 생성
- RecyclerView구현했으나 디자인 매우이상함
- 기존 데이터 클릭시 수정화면으로 진입.(AlarmDetailActivity수정해서 사용)
- 수정화면에서 수정후 데이터 저장
- 요일 선택 완료
- RecyclerView item에서 시계모양 클릭시 데이터베이스 조정하기
- BroadCastListener만들기
- BroadCastListener에서 service 호출하기
- Service에서 Notification 생성 후 등록하기
- RecyclerViewItem 디자인수정해야함
- ForegroundService로 실행
- Notification에서 바로 끌 수 있는 Action 등록
- Service에서 음악 재생 및 종료 만듬
- 알람끄는 페이지 생성
- RecyclerView item에서 시계모양 클릭시 AlarmManager 조정하기
- 알람 repeat 속성 등록하기
- 알람 요일에 따라 그만큼 만들고 등록하기 (기존 id * 10 + 0~6(요일에따라) 로 PendingIntent의 code를 정해주자)
- 알람 취소 생성(요일별 정보를 들고 있다가 for를 돌면서 id * 10 + 0~6를 만들어서 취소시키기)
- 알람 재거시 등록취소 확인하기
- ContentProvider 생성
- alarm등록 static으로 만들어서 여기저기서 사용 할 수 있게 만들고 ContentProvider에 업데이트하기
- 저장한 메모 가져오기
- 반복로직만들기
- 재부팅시 재등록 로직 만들기
- 진동구현 <에뮬레이터밖에 못써서 디버깅 제대로 못함>
- 알람 소리 적용

- 알람 울릴시 위치 or 날시정보가져오게 만들기 <에뮬레이터밖에 못써서 디버깅 제대로 못함 날씨정보는 잘 가져옴>
- provider 확인 <query만>;
- 알람 울리고 나면 반복 설정 안해 둔 경우 알람 끄기<- 아니면 재부팅 할 때마다 다시됨. 모든요일 다 꺼지면 비활성화
##### todo
- clean code
#### issue
- ListActivity에서 onCreate, swipe일 때만 changeNotify하는게 맞는가
- 왜 swipe하면 순서가 달라지는가?

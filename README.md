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
- clean code

##### todo

#### issue

- ListActivity에서 onCreate, swipe일 때만 changeNotify하는게 맞는가
- 왜 swipe하면 순서가 달라지는가?

## 과제 설명

### 기능

#### 1. 리스트페이지, 상세페이지 두가지 페이지 제공

- 리스트페이지에서 플로팅 버튼을 눌러 상세페이지 접근 가능

#### 2. 알람 추가 / 수정 / 삭제 / 조회 가능

- 상세페이지에서 생성 누르면 알람 추가 가능
- 리스트페이지에서 보고싶은 알람 목록을 누르면 조회 및 수정 페이지 제공
- 리스트페이지에서 swipe로 삭제가능

#### 3. 데이터는 오픈소스 Realm이용

### 요구사항

#### 1. 버전

- minSdkVersion 15
- targetSdkVersion 23
- buildToolsVersion 25.0.2

#### 2. AlarmManager로 BroadCast를 보내고 다시 Service를 실행하여 알람 시작

- service는 ForegroundService 사용

#### 3. 알람 요일 다수 선택 가능 및 반복 선택 가능

- 원하는 요일들과 반복 여부 선택 가능

#### 4. 바로 끌 수 있는 Action을 가진 Notificaion 제공, Notification을 누를 시 날씨정보를 볼 수 있는 Activity 제공

#### 5. 알람 동작시 진동, 소리 제공

- 진동은 에뮬레이터의 한계로 코딩은 했으나 테스트 불가
- 소리는 미디어 대신 알람 볼륨을 사용, 볼빨사의 좋다고말해 재생

### 옵션

#### 1. 오픈소스 사용

- Realm, ButterKnife, Retrofit, Gson 사용

#### 2. App 재부팅시 정상작동

- BroadCastReceiver로 BOOT_COMPLETE intent를 받아 활성화된 알람 모두 등록

#### 3. 메모 및 위치, 날씨 정보 포함

- 알람 등록시 메모작성하면 Notification 및 알람Activity에서 볼 수 있다.
- 위치정보는 현재 GPS나 Network가 켜져 있을 경우 자기위치를 가져온다 켜져있지 않을경우 서초가 기본값. 에뮬레이터의 한계로 코딩은 했으나 테스트 미흡
- SK Weather Planet API이용(앞의 위치에서 위도, 경도를 보낸다)하여 위치, 날씨, 기온 정보 제공

#### 4. ContentProvider를 이용

- 다른어플에서 전체 알람 조회 가능, 간단한 테스트 어플이 함께 저장되어있다. (insert, delete, update 도 만들었지만 query만 기능확인)

### 그 외 설명

#### 1. 리스트에서 시계이미지 누르면 활성, 비활성화 가능

### 한계

- 한번만 울리는 알람의 경우 디바이스가 꺼져있는 상황에서 알람시간이 될 경우 비활성화 되지않고 여전히 활성화, 디바이스가 켜져있을 알람이 울려야지 취소되게 만들었음.
- 설연휴라 휴대폰으로 테스트 할 수 있는 환경이 되지않아 에뮬레이터만으로 테스트 할 수 없는 경우가 있어 아쉬움.

# ComposeMVVMTEST
 
## 1. Compose Metrics를 활용하여 Compose Skipping을 시켰습니다.
확인하시려면 ./gradlew :feature-main:releaseComposeCompilerHtmlReport 이후에   feature-main/build/compose_report/index.html을 확인하시면 됩니다.



## 2. HorizontalGrid와 LazyColumn 중 트레이드오프에서 LazyColumn을 선택했습니다.
둘다 Lazy하기 때문에 같이 적용이 불가능한데, Grid를 상위 컴포저블로 둘경우, 하위 item{}에 Fixed(1) 이라는 모디파이어 연산자가 필수적이고, 좀더 화면을 이쁘게 만들기위해 skickyHeader를 위해 LazyColumn을 채택하였습니다.


## 3. 도메인 엔티티와 UI모델을 분리하였습니다.
컴포즈가  Skipping을 하기위해선, 컴포즈 컴파일러가 작동되는곳에 클래스가 위치해야만 건너뛸수 있습니다.
하지만 도메인 모듈은 일반적으로 코틀린 모듈로 만들기 때문에, UI Module에서 한번 Mapper한뒤, Skipping 능력을 얻을수 있습니다.


## 4. UseCase를 배제하였습니다
복잡한 비즈니스 로직이 아니기 때문에 배제하였습니다.

## 5. Horizontal Pager로 Parallax 애니메이션을 구현하였습니다.


## 6. Header를 클릭하면, 3개씩 데이터가 표출되는 로직을 구현하였습니다.
구현은 하였지만, 뭔가 아쉬움이 남습니다.

## 7. data feature:main에 Unit Test를 작성하였습니다.
JUnit5 와 Turbin을 사용하여 작성하였습니다.


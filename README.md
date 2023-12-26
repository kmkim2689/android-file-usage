# Dealing With Files In Compose

## 목적
### 실제 프로젝트를 진행하다 보면, 다음과 같은 유즈케이스를 구현해야 함
* 파일을 서버로 전송
  * 이미지, pdf 파일 등

## 방법
* 서버로 보내기 전, 먼저 actual file path로부터 추출하여 File이라는 객체를 활용하여 구축

### 필요 구현 사항
* Main Screen : ui를 보여줌
* Ui State : Main Screen의 State를 구현하기 위한 데이터 클래스 
* MainViewModel : UiState를 보유하고 변경
* Uri Path Finder : Uri로부터 실제 경로를 가져오기 위한 클래스

### 요구사항
* Compose Version >= 1.1.1
* Kotlin Version >= 1.6.10

### 다양한 형식의 파일을 디바이스로부터 가져오는 방법
* actual path를 가져오기
* 여러 개의 파일을 가져오고자 함

1. 다음의 의존성을 추가:
```
//permission handling in compose
implementation 'com.google.accompanist:accompanist-permissions:0.24.13-rc'

//for view-model in compose
implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0"
```

2. AndroidManifest 파일에 외부 저장소 읽기에 대한 권한 설정을 추가한다.
* 몇몇 안드로이드 버전에서의 crash를 방지하기 위함
```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

3. UriPathFinder 클래스 구현
* Uri로부터 실제 경로를 가져오는 기능을 구현
* UriPathFinder.kt

4. UiState
* 다수의 path 정보로 이뤄진 string의 리스트

5. ViewModel
* hold, update the state
* **uri to path conversion logic**

### 이슈
* https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts
* https://stackoverflow.com/questions/68800942/not-getting-result-from-rememberlauncherforactivityresult-in-jetpack-compose
* image picking : https://fvilarino.medium.com/using-activity-result-contracts-in-jetpack-compose-14b179fb87de
## 1. Pdf picker with the help of result launchers
* Fab 버튼을 통해 파일 관리자 상에서 pdf 파일을 선택할 수 있도록 한다.
* 업로드 : 파일을 선택하여 앱 상으로 가져온 후, upload pdf 버튼을 누르면 firebase 데이터베이스 상으로 전달되도록 한다.
* 불러오기 : show all 버튼을 클릭하면, firebase 상에 저장되어 있는 모든 pdf 파일들을 열람할 수 있도록 한다.
    * 파일 선택 시, 앱 상에서 pdf 파일을 열람할 수 있다.
    * pdf 파일을 열람하는 화면 상 fab 버튼 클릭 시, 해당 pdf 파일을 다운로드 할 수 있도록 한다.

### 구현 과정
1. 안드로이드 프로젝트 상에 Firebase 연결 - 기본적인 설정을 위함
* Tools > Firebase > Realtime Database(실시간 데이터베이스 적용을 위함) > Get started with Realtime Database
* Firebase 상에서 프로젝트를 생성하면 기본적인 연결 완료(google-services.json 파일이 자동으로 들어가게 된다.)
* "Add the Realtime Database SDK to your app" 실행
  * firebase realtime database를 위한 여러 의존성들이 앱에 설정된다.
* firebase를 활용하기 위해, storage가 필요하다.
  * Tools > Firebase > Cloud Storage For Firebase > Get started with Cloud Storage
  * "Add the Cloud Storage SDK to your app"
* 제반 설정이 완료된 이후, firebase console의 Realtime Database 페이지에서 데이터베이스 정보 확인 가능
  * Creating Database
  * All products(모든 제품) > Realtime Database > Create Database(데이터베이스 만들기)
    * 데이터베이스 서버 위치를 설정하고, 보안 규칙을 "테스트 모드에서 시작"으로 선택한다.
  * All products(모든 제품) > Storage > Get Started
    * "테스트 모드에서 시작" 선택
  -> real time database와 storage에 대한 설정 완료

2. Pdf Picker Floating Button 구현

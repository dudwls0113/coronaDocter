<?php
//error_reporting(E_ALL); ini_set("display_errors", 1);

require './pdos/DatabasePdo.php';
require './pdos/IndexPdo.php';
require './pdos/SeatPdo.php';
require './pdos/CoronaPdo.php';

require './vendor/autoload.php';

use \Monolog\Logger as Logger;
use Monolog\Handler\StreamHandler;
use Monolog\Handler\RotatingFileHandler;

date_default_timezone_set('Asia/Seoul');
ini_set('default_charset', 'utf8mb4');

//에러출력하게 하는 코드

//Main Server API
$dispatcher = FastRoute\simpleDispatcher(function (FastRoute\RouteCollector $r) {
//    echo 122;
    /* ******************   Test   ****************** */
    $r->addRoute('GET', '/', ['IndexController', 'index']); //index
    $r->addRoute('POST', '/email', ['IndexController', 'email']); //email- 회원가입 API
    $r->addRoute('POST', '/nickname', ['IndexController', 'nickname']); //nickname - 회원가입 API
    $r->addRoute('POST', '/guest', ['IndexController', 'guest']); //회원가입 API - 토큰 생성 API
    $r->addRoute('POST', '/url', ['IndexController', 'Certified']); //학교인증 API
    $r->addRoute('POST', '/area', ['IndexController', 'postArea']); //관심지역 설정 API
    $r->addRoute('POST', '/user', ['IndexController', 'login']); //로그인 API
    $r->addRoute('GET', '/area', ['IndexController', 'getArea']); //관심지역 출력 API
    $r->addRoute('GET', '/user', ['MyController', 'user']); //유저정보 출력 API
    $r->addRoute('PATCH', '/detailUser', ['MyController', 'patchUser']); //유저상세정보 수정 API
    $r->addRoute('GET', '/detailUser', ['MyController', 'detailUser']); //유저상세정보 조회 API
    $r->addRoute('GET', '/likeArea', ['MyController', 'myArea']); // 관심지역 조회 API

    $r->addRoute('POST', '/home', ['MainController', 'home']); // 피드 조회 API
    $r->addRoute('GET', '/post/{postNo}', ['MainController', 'getPost']); //게시글 상세보기 조회 API
    $r->addRoute('GET', '/post/{postNo}/comments', ['MainController', 'getComments']); //댓글 조회 API
    $r->addRoute('GET', '/post/{postNo}/comments/{commentsNo}', ['MainController', 'getBundle']); //대댓글 조회 API
    $r->addRoute('POST', '/post/{postNo}/comments', ['MainController', 'postComments']); //댓글 작성, 대댓글 작성 API
    $r->addRoute('POST', '/post', ['MainController', 'postPost']); //게시글 작성 API
    $r->addRoute('POST', '/like/{postNo}', ['MainController', 'postLike']); //좋아요 누르기 API
    $r->addRoute('DELETE', '/like/{postNo}', ['MainController', 'deleteLike']); //좋아요 취소 API
    $r->addRoute('POST', '/scrap/{scrapNo}/post/{postNo}', ['MainController', 'doScrap_post']); //게시글 스크랩 하기 API
    $r->addRoute('DELETE', '/scrap/{scrapNo}/post/{postNo}', ['MainController', 'deleteScrap_post']); //게시글 스크랩 해제 API
    $r->addRoute('DELETE', '/scrap/post/{postNo}', ['MainController', 'deleteScrap_post_no_scrap']); //게시글 스크랩 해제 API (홈화면용 scrap No 필요없)
    $r->addRoute('POST', '/search/post', ['MainController', 'searchPost']);  //게시글 검색 API
    $r->addRoute('POST', '/search/nickname', ['MainController', 'searchNick']);  //닉네임 검색 API
    $r->addRoute('PATCH', '/post/{postNo}', ['MainController', 'patchPost']);  //게시글 수정하기 API
    $r->addRoute('DELETE', '/post/{postNo}', ['MainController', 'deletePost']);  //게시글 삭제하기 API

    $r->addRoute('GET', '/scrap/{scrapNo}/{page}', ['MainController', 'getscrapHome']); //스크랩북 피드 조회 API
    $r->addRoute('POST', '/scrap', ['MyController', 'postScrap']); //스크랩북 추가 (생성) API
    $r->addRoute('GET', '/scrap', ['MyController', 'getScrap']); //스크랩북 목록 조회 API
    $r->addRoute('PATCH', '/scrap/{scrapNo}', ['MyController', 'patchScrap']); //스크랩북 수정 API
    $r->addRoute('DELETE', '/scrap/{scrapNo}', ['MyController', 'deleteScrap']);  //스크랩북 삭제하기 API

    $r->addRoute('GET', '/myPost/{page}', ['MyController', 'getMyPost']); //나의 피드 조회 API
    $r->addRoute('GET', '/myGallery/{page}', ['MyController', 'getMyGallery']); //나의 갤러리 조회 API
    $r->addRoute('GET', '/user/profile/{userNo}', ['UserController', 'getUserProfile']); //타인 개인정보 조회 API
    $r->addRoute('GET', '/userPost/{userNo}/{page}', ['UserController', 'getUserPost']); //타인 포스트 조회 API
    $r->addRoute('GET', '/userGallery/{userNo}/{page}', ['UserController', 'getUserGallery']); //타인 갤러리 조회 API
    $r->addRoute('GET', '/user/scrap/{userNo}', ['UserController', 'getUserScrapList']); //타인 스크랩북 목록 조회 API

    $r->addRoute('GET', '/pushHistory', ['UserController', 'pushHistory']); // 푸시 조회 API
    $r->addRoute('PATCH', '/users/fcmToken', ['UserController', 'updateFCMToken']);
    $r->addRoute('GET', '/autoLogin', ['MainController', 'autoLogin']);  //자동 로그인 api
    $r->addRoute('GET', '/test/{postNo}', ['MainController', 'testPostNo']);  //게시글 유효성 검사 API
    $r->addRoute('GET', '/password', ['UserController', 'getPass']);  //비밀번호 찾기 API [개발전]
    $r->addRoute('POST', '/isAcceptPush', ['UserController', 'updateAcceptPush']); //푸시여부 업데이트 API
    $r->addRoute('GET', '/isAcceptPush', ['UserController', 'getAcceptPush']); //푸시여부 업데이트 API

    $r->addRoute('GET', '/version/{versionCode}', ['UserController', 'checkUpdate']); //업데이트 체크 API
    $r->addRoute('GET', '/logout', ['UserController', 'logout']); //로그아웃 API (fcm안보내기 위함)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    $r->addRoute('GET', '/seat', ['SeatController', 'getSeat']);
    $r->addRoute('POST', '/seat', ['SeatController', 'postSeat']);
    $r->addRoute('POST', '/lbUser', ['SeatController', 'signUp']);
    $r->addRoute('GET', '/lbUser', ['SeatController', 'getUser']);
    $r->addRoute('POST', '/lbUser/verify', ['SeatController', 'verifyUser']);

    $r->addRoute('POST', '/reservation', ['SeatController', 'reservation']);
    $r->addRoute('POST', '/reservationRequest', ['SeatController', 'reservationRequest']);
    $r->addRoute('POST', '/soundRequest', ['SeatController', 'soundRequest']);

    $r->addRoute('GET', '/lbUser/{userNo}', ['SeatController', 'getUserDetail']);
    $r->addRoute('POST', '/isVerify', ['SeatController', 'isVerify']);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Corona Doctor//
    $r->addRoute('GET', '/corona/route', ['CoronaController', 'getRoute']);
    $r->addRoute('GET', '/corona/clinic', ['CoronaController', 'getClinic']);
    $r->addRoute('GET', '/corona/hospital', ['CoronaController', 'getHospital']);
    $r->addRoute('GET', '/corona/statistic', ['CoronaController', 'getStatistic']);
    $r->addRoute('GET', '/corona/update/{versionCode}', ['CoronaController', 'getUpdate']);
    $r->addRoute('GET', '/corona/fcm/{fcmToken}', ['CoronaController', 'insertFcmToken']);
    $r->addRoute('POST', '/corona/push', ['CoronaController', 'pushMessage']);
    $r->addRoute('POST', '/corona/push/desc', ['CoronaController', 'pushMessageDesc']);

    $r->addRoute('GET', '/corona/infected', ['CoronaController', 'getInfected']);
    $r->addRoute('POST', '/corona/pushTest', ['CoronaController', 'pushTest']);

    $r->addRoute('POST', '/corona/post', ['CoronaController', 'post']);
    $r->addRoute('POST', '/corona/comment', ['CoronaController', 'postComment']);

    $r->addRoute('GET', '/corona/post/{page}', ['CoronaController', 'getPost']);
    $r->addRoute('GET', '/corona/hotPost', ['CoronaController', 'getHotPost']);

    $r->addRoute('GET', '/corona/postDetail/{postNo}', ['CoronaController', 'getPostDetail']);
    $r->addRoute('POST', '/corona/like', ['CoronaController', 'likePost']);

    $r->addRoute('GET', '/corona/sponsor', ['CoronaController', 'getSponsor']);
    $r->addRoute('GET', '/corona/graph', ['CoronaController', 'getGraph']);

    $r->addRoute('GET', '/corona/pushScheduler', ['CoronaController', 'pushScheduler']);

    $r->addRoute('POST', '/corona/declaration', ['CoronaController', 'declaration']);

//    $r->addRoute('GET', '/corona/post', ['CoronaController', 'getPost']);




//    $r->addRoute('GET', '/test/{testNo}', ['IndexController', 'testDetail']);
//    $r->addRoute('POST', '/test', ['IndexController', 'testPost']);
//    $r->addRoute('POST', '/jwt', ['MainController', 'createJwt']);
//    $r->addRoute('GET', '/users', 'get_all_users_handler');
//    // {id} must be a number (\d+)
//    $r->addRoute('GET', '/user/{id:\d+}', 'get_user_handler');
//    // The /{title} suffix is optional
//    $r->addRoute('GET', '/articles/{id:\d+}[/{title}]', 'get_article_handler');
});

// Fetch method and URI from somewhere
$httpMethod = $_SERVER['REQUEST_METHOD'];
$uri = $_SERVER['REQUEST_URI'];

// Strip query string (?foo=bar) and decode URI
if (false !== $pos = strpos($uri, '?')) {
    $uri = substr($uri, 0, $pos);
}
$uri = rawurldecode($uri);

$routeInfo = $dispatcher->dispatch($httpMethod, $uri);

// 로거 채널 생성
$accessLogs = new Logger('ACCESS_LOGS');
$errorLogs = new Logger('ERROR_LOGS');
// log/your.log 파일에 로그 생성. 로그 레벨은 Info
$accessLogs->pushHandler(new RotatingFileHandler('logs/access.log', Logger::INFO));
$errorLogs->pushHandler(new StreamHandler('logs/errors.log', Logger::ERROR));
// add records to the log
//$log->addInfo('Info log');
// Debug 는 Info 레벨보다 낮으므로 아래 로그는 출력되지 않음
//$log->addDebug('Debug log');
//$log->addError('Error log');

switch ($routeInfo[0]) {
    case FastRoute\Dispatcher::NOT_FOUND:
        // ... 404 Not Found
        echo "404 Not Found";
        break;
    case FastRoute\Dispatcher::METHOD_NOT_ALLOWED:
        $allowedMethods = $routeInfo[1];
        // ... 405 Method Not Allowed
        echo "405 Method Not Allowed";
        break;
    case FastRoute\Dispatcher::FOUND:
        $handler = $routeInfo[1];
        $vars = $routeInfo[2];

        switch ($routeInfo[1][0]) {
            case 'IndexController':
                $handler = $routeInfo[1][1];
                $vars = $routeInfo[2];
                require './controllers/IndexController.php';
                break;
            case 'MainController':
                $handler = $routeInfo[1][1];
                $vars = $routeInfo[2];
                require './controllers/MainController.php';
                break;
            case 'MyController':
                $handler = $routeInfo[1][1]; $vars = $routeInfo[2];
                require './controllers/MyController.php';
                break;
            case 'UserController':
                $handler = $routeInfo[1][1]; $vars = $routeInfo[2];
                require './controllers/UserController.php';
                break;

            case 'SeatController':
                $handler = $routeInfo[1][1]; $vars = $routeInfo[2];
                require './controllers/SeatController.php';
                break;

            case 'CoronaController':
                $handler = $routeInfo[1][1]; $vars = $routeInfo[2];
                require './controllers/CoronaController.php';
                break;/*
            case 'ElementController':
                $handler = $routeInfo[1][1]; $vars = $routeInfo[2];
                require './controllers/ElementController.php';
                break;*/
            case 'HackController':
                $handler = $routeInfo[1][1]; $vars = $routeInfo[2];
                require './controllers/HackController.php';
                break;
        }

        break;
}

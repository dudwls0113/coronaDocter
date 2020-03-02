<?php
require 'function.php';

const JWT_SECRET_KEY = "TEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEY";
const FCM_SERVER_KEY = "AAAAlvHpLX0:APA91bFragmSbcQL3AxalcKWt0rtp-8TXcvvSnQIWWleoMJlzqtAZhfYmqo3CvuKi5aFMDL2COTKVMO4feMd_dSQQ-4jhv0DlLUddpJ7TicFkjaSa-QcR0KZTNiDofXDc8SypOZUpiow";

$res = (Object)Array();
header('Content-Type: json');
$req = json_decode(file_get_contents("php://input"));
try {
    addAccessLogs($accessLogs, $req);
    switch ($handler) {

        case "updateFCMToken":
            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];
            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];

            $userNo = convert_to_userNo($email);

//            echo "$userNo";

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            }

            $token = $req->FCMToken;
            http_response_code(200);

            $res->isSuccess = updateFCMToken($userNo, $token);
            $res->code = 100;
            $res->message = "성공";
            echo json_encode($res, JSON_NUMERIC_CHECK);
            break;

        case "pushHistory":
            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];
            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];

            $userNo = convert_to_userNo($email);

//            echo "$userNo";

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            }
            http_response_code(200);


            $res->result = getPushHistory($userNo);
            $res->code = 100;
            $res->message = "성공";
            echo json_encode($res, JSON_NUMERIC_CHECK);
            break;

        case "getUserProfile":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];

//            $userNo = convert_to_userNo($email);
            $userNo = $vars["userNo"];

//            echo "$userNo";

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                http_response_code(200);
                $res->result = getUser($userNo);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "유저정보 조회를 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "getUserPost":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $page = (int)$vars["page"];

            $myUserNo = convert_to_userNo($email);
            $userNo = $vars["userNo"];

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                $getMyPost = getOtherUserFeed($userNo, $myUserNo, $page);
                $is_check_Fesd = is_check_Fesd($userNo);

                if ($is_check_Fesd == 0) {
                    $res->isSuccess = TRUE;
                    $res->code = 202;
                    $res->message = "피드 없음";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }
                if ($getMyPost == false) {
                    $res->isSuccess = False;
                    $res->code = 200;
                    $res->message = "내 피드조회를 실패";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                } else {
                    $res->result = $getMyPost;
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "내 피드조회 성공";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                }
                return;

            }

            break;

        case "getUserGallery":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $page = (int)$vars["page"];

            $userNo = $vars["userNo"];

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                $getMyPost = getGallery($userNo, $page);
                $is_check_gallery = is_check_gallery($userNo);

                if ($is_check_gallery == 0) {
                    $res->isSuccess = TRUE;
                    $res->code = 202;
                    $res->message = "갤러리 없음";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if ($getMyPost == false) {
                    $res->isSuccess = False;
                    $res->code = 200;
                    $res->message = "내 갤러리 실패";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                } else {
                    $res->result = $getMyPost;
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "내 피드조회 성공";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                }
                return;

            }

            break;

        case "getUserScrapList":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $userNo = $vars["userNo"];
//            $page = $req->page;

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {

                $getUserScrap = getUserScrap($userNo);
                $is_exist_scrap = scrapCheck2($userNo);

                if ($is_exist_scrap == 0) {
                    $res->isSuccess = TRUE;
                    $res->code = 202;
                    $res->message = "스크랩북 없음";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;

                }

                if ($getUserScrap == false) {
                    $res->isSuccess = False;
                    $res->code = 200;
                    $res->message = "타인 스크랩북 목록 조회 실패";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;

                } else {
                    $res->result = $getUserScrap;
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "타인 스크랩북 목록 조회 성공";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;

                }

            }
            break;

        case "getPass": //개발전

            $email = $req->email;
            $name = $req->name;
            $birth = $req->birth;

            if (strlen($email) < 1) {
                $res->isSuccess = False;
                $res->code = 160;
                $res->message = "이메일을 입력해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            if (strlen($name) < 1) {
                $res->isSuccess = False;
                $res->code = 161;
                $res->message = "이름을 입력해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            if (strlen($birth) < 1) {
                $res->isSuccess = False;
                $res->code = 162;
                $res->message = "생년월일을 입력해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            if (strlen($email) > 0 and strlen($name) > 0 and strlen($birth) > 0) {
//                $checkpass = checkPass($email, $name, $birth);
//                if ($checkpass == true) {
//                    $res->isSuccess = False;
//                    $res->code = 200;
//                    $res->message = "임시 비밀번호 작성";
//                    echo json_encode($res, JSON_NUMERIC_CHECK);
//                } else {
//                    $res->result = $checkpass;
//                    $res->isSuccess = TRUE;
//                    $res->code = 100;
//                    $res->message = "비밀번호 찾기 에러";
//                    echo json_encode($res, JSON_NUMERIC_CHECK);
//                }
//                return;
            }

            break;

        //푸시동의여부 업데이트 API
        case "updateAcceptPush":
            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            }
            $accept = $req->accept;
            $result = updateAcceptPush($userNo, $accept);

            if ($result == false) {
                $res->code = 200;
                $res->message = "푸시여부 업데이트 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "푸시여부 업데이트 성공";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //푸시동의여부 조회 API
        case "getAcceptPush":
            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            }
            $result = isAcceptedPush($userNo);
            if ($result === 999) {
                $res->code = 200;
                $res->message = "푸시여부 조회 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "푸시여부 조회 성공";
                if($result){
                    $res->isAcceptedPush = 1;
                }
                else{
                    $res->isAcceptedPush = 0;
                }
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //업데이트 체크 API
        case "checkUpdate":
            $versionCode = $vars["versionCode"];
            $result = checkVersionCode($versionCode);

            if ($result == false) {
                $res->code = 200;
                $res->message = "업데이트 체크 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "업데이트 체크 성공";
                $res->updateMode = $result;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //업데이트 체크 API
        case "logout":
            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            }

            $result = logout($userNo);

            if ($result == false) {
                $res->code = 200;
                $res->message = "로그아웃에 실패했습니다. 다시 시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "로그아웃 성공";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;
    }
} catch (\Exception $e) {
    return getSQLErrorException($errorLogs, $e, $req);
}

<?php
require 'function.php';
$res = (Object)Array();
header('Content-Type: json');
$req = json_decode(file_get_contents("php://input"));
try {
    addAccessLogs($accessLogs, $req);
    switch ($handler) {

        case "getRoute":
            $infectedNoList = getHaveRouteInfectedList();

            $result_arr = array();

            foreach ($infectedNoList as $row) {
                $obj = (Object)Array();
                $infectedNo = $row['infectedNo'];
                $routeList = getRoute($infectedNo);
                $obj->route = $routeList;
                array_push($result_arr, $obj);
            }

            $res->code = 100;
            $res->message = "성공";
            $res->result = $result_arr;

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "getClinic":
            $result = getClinic();
            $res->code = 100;
            $res->message = "성공";
            $res->result = $result;

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "getHospital":
            $result = getHospital();
            $res->code = 100;
            $res->message = "성공";
            $res->result = $result;

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "getStatistic":
            $result = getStatistic();
            $res->code = 100;
            $res->message = "성공";
            $res->result = $result;

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        //업데이트 체크 API
        case "getUpdate":
            $versionCode = $vars["versionCode"];
            $result = checkCoronaDoctorVersionCode($versionCode);

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

        //FCM 등록 API
        case "insertFcmToken":
            $token = $vars["fcmToken"];
            $result = updateCoronaFcmToken($token);

            if ($result === 999) {
                $res->code = 200;
                $res->message = "FCM 업데이트 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "FCM 업데이트 성공";
                $res->fcmToken = $token;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //푸시전송 APO
        case "pushMessage":
//            $title = $req->title;
//            $message = $req->message;
            $pushNo = $req->pushNo;

            $fcmArr = getFcmTokenArr($pushNo);
            $result = sendPushMessage($fcmArr, $pushNo);

            if ($result > 0) {
                $res->code = 100;
                $res->message = "푸시전송 성공";
                $res->count = $result;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 200;
                $res->message = "푸시전송 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //푸시전송 DESC
        case "pushMessageDesc":
//            $title = $req->title;
//            $message = $req->message;
            $pushNo = $req->pushNo;

            $fcmArr = getFcmTokenArrDESC($pushNo);
            $result = sendPushMessage($fcmArr, $pushNo);

            if ($result > 0) {
                $res->code = 100;
                $res->message = "푸시전송 성공";
                $res->count = $result;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 200;
                $res->message = "푸시전송 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //감염자 목록 확인 
        case "getInfected":
            $result = getInfected();

            if ($result > 0) {
                $res->code = 100;
                $res->message = "성공";
                $res->count = $result;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 200;
                $res->message = "실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //푸시테스트
        case "pushTest":
            $title = $req->title;
            $message = $req->message;
            $fcmToken = $req->fcmToken;
            $type = $req->type;
            $postNo = $req->postNo;

            $result = sendPushTest($fcmToken, $title, $message, $type, $postNo);

            if ($result > 0) {
                $res->code = 100;
                $res->message = "푸시전송 성공";
                $res->count = $result;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 200;
                $res->message = "푸시전송 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //글작성
        case "post":
            $title = $req->title;
            $content = $req->content;
            $fcmToken = $req->fcmToken;

            $userNo = getUserNoByToken($fcmToken);
            if (strlen($title) < 2) {
                $res->code = 203;
                $res->message = "제목을 2자 이상 입력해주세요.";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }
            if (strlen($content) < 5) {
                $res->code = 203;
                $res->message = "내용을 5자 이상 입력해주세요.";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }
            if ($userNo < 0) { // 없는 토큰
                $res->code = 201;
                $res->message = "유효하지 않은 접근입니다 다시시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            $result = insertPost($userNo, $title, $content);

            if ($result === 999) {
                $res->code = 200;
                $res->message = "작성 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "작성 성공";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //댓글작성
        case "postComment":
            $content = (string)$req->content;
            $postNo = $req->postNo;
            $fcmToken = $req->fcmToken;

            $userNo = getUserNoByToken($fcmToken);
            if (strlen($content) < 2) {
                $res->code = 203;
                $res->message = "댓글을 2자 이상 입력해주세요.";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }
            if ($userNo < 0) { // 없는 토큰
                $res->code = 201;
                $res->message = "유효하지 않은 접근입니다 다시시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }
            if (isValidPostNo($postNo) < 0) {
                $res->code = 202;
                $res->message = "유효허지 않은 게시물입니다 다시 시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            $result = insertComment($postNo, $userNo, $content);

            if ($result === 999) {
                $res->code = 200;
                $res->message = "작성 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "작성 성공";
                if (getUserTokenByPost($postNo) != $fcmToken) {
                    sendPushAndLogging("코로나닥터", "내 글에 댓글이 달렸습니다.", getUserTokenByPost($postNo), $userNo, $postNo);
                }

                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //게시글 조회
        case "getPost":
            $page = $vars["page"];
            $result = getCoronaPost((int)$page);

            if ($result === 999) {
                $res->code = 200;
                $res->message = "조회 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "조회 성공";
                $res->result = $result;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //인기 게시글 조회
        case "getHotPost":
            $result = getCoronaHotPost();
            if ($result === 999) {
                $res->code = 200;
                $res->message = "조회 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "조회 성공";
                $res->result = $result;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;


        //게시글 상세 조회
        case "getPostDetail":
            $postNo = $vars["postNo"];
            if (isValidPostNo($postNo) < 0) {
                $res->code = 202;
                $res->message = "유효허지 않은 게시물입니다 다시 시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            $result = getCoronaPostDetail($postNo);
            $Comment = getCoronaComment($postNo);
            if ($result === 999) {
                $res->code = 200;
                $res->message = "조회 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "조회 성공";
                $res->result = $result;
                $res->comment = $Comment;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

        //게시글 좋아
        case "likePost":
            $postNo = $req->postNo;
            $fcmToken = $req->fcmToken;
            $userNo = getUserNoByToken($fcmToken);

            if ($userNo < 0) { // 없는 토큰
                $res->code = 201;
                $res->message = "유효하지 않은 접근입니다 다시시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }
            if (isValidPostNo($postNo) < 0) {
                $res->code = 202;
                $res->message = "유효허지 않은 게시물입니다 다시 시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }
            if (isValidLike($postNo, $userNo) < 0) {
                $res->code = 204;
                $res->message = "이미 좋아요를 누른 게시글입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            $result = updateLike($postNo, $userNo);
            if ($result === 999) {
                $res->code = 200;
                $res->message = "좋아요 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "좋아요 성공";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                if (getUserTokenByPost($postNo) != $fcmToken) {
                    sendPushAndLogging("코로나닥터", "내 글에 좋아요가 눌렸습니다", getUserTokenByPost($postNo), $userNo, $postNo);
                }
            }
            break;

        case "getSponsor":
            $result = getSponsor();
            $res->code = 100;
            $res->message = "성공";
            $res->result = $result;

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "getGraph":
            $result = getGraph();
            $res->code = 100;
            $res->message = "성공";
            $res->result = $result;

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;


//            1분마다 스케줄러 돌기 -> 푸시보낼게있으면 300명만 조회해서 푸시전송?
        case "pushScheduler":
            $pushInfo = getLastPushInfo();
            echo json_encode($pushInfo, JSON_NUMERIC_CHECK);

            $fcmArr = getFcmTokenArr300($pushInfo);

            $result = sendPushMessageForScheduler($fcmArr, $pushInfo);

            if ($result > 0) {
                $res->code = 100;
                $res->message = "푸시전송 성공";
                $res->count = $result;
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 200;
                $res->message = "푸시전송 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;

            break;


        //게시글 신고
        case "declaration":
            $postNo = $req->postNo;
            $fcmToken = $req->fcmToken;
            $userNo = getUserNoByToken($fcmToken);

            if ($userNo < 0) { // 없는 토큰
                $res->code = 201;
                $res->message = "유효하지 않은 접근입니다 다시시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }
            if (isValidPostNo($postNo) < 0) {
                $res->code = 202;
                $res->message = "유효허지 않은 게시물입니다 다시 시도해주세요";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }
            if (isValidDeclaration($postNo, $userNo) < 0) {
                $res->code = 204;
                $res->message = "이미 신고한 게시글입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            $result = updateDeclaration($postNo, $userNo);
            if ($result === 999) {
                $res->code = 200;
                $res->message = "신고 실패";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            } else {
                $res->code = 100;
                $res->message = "신고 성공";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }
            break;
    }

} catch (\Exception $e) {
    echo $e;
    return getSQLErrorException($errorLogs, $e, $req);
}

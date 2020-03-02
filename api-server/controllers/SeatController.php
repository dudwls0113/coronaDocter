<?php
require 'function.php';

//const JWT_SECRET_KEY = "TEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEY";
const FCM_SERVER_KEY2 = "AAAAyfQnEDY:APA91bER5EES68LiTjM9__mIcUDF0sXE1azFDyNXoLIajUU2wS-dvnTdkolwEgFNO7RmKJT4E8L7HH9areL2ffIhUiFR3NPQwx802Qek0lss2EnUl4DCyVw8Xk-T-TMmRD3A0xgfMRit";

$res = (Object)Array();
header('Content-Type: json');
$req = json_decode(file_get_contents("php://input"));
try {
    addAccessLogs($accessLogs, $req);
    switch ($handler) {

        case "getSeat":
            $res->code = 100;
            $res->message = "성공";
            $res->result = getSeat();

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "postSeat":
            $seatNo = $req->seatNo;
            $status = $req->status;

            $result = getSeat();
            $seat1 = $result[0]['status'];
            $seat2 = $result[1]['status'];

//            echo $seatNo, $status;

            if ($seatNo == 1 && $status == 1) { //1번 좌석에 사람이 앉은경우
                if ($seat1 == 3) { // 예약된 좌석일경우
                    //폰에게 fcm전송해서 확인받기, 임산부가 맞으면 status바꿔주고 알림끄기, 아니면 그냥두기

                    $timeDiff = pushTimeCheck();

                    if($timeDiff >3){
                        $data = array("title" => "Love Baby",
                            "body" => "예약된 좌석에 사람이 감지되어있습니다.",
                            "message" => "예약한 좌석에 앉으셨나요?",
                            "seatNo" => $seatNo);

                        try {
//                        $result = sendFcm("c2umRG18drQ:APA91bE6GSFj--mXUJthwtm9cpH9wiLx9IOLSmzorKCjpfeRupFgigGQtmugIqZhNeC3iyVUWg8l735ECO06cQWitlhSU691PQRxJlJXMuv0DCqUykJUPAfzLJLLax9XQM4UJcWsUfHn", $data, FCM_SERVER_KEY2, 'ANDROID');
                            $result = sendFcm("f38ifyZxI0U:APA91bGADmUNfZm255OGYGxJr_1X-bphsa0KL_XTaDyj7INEy5WF5fifQY3IYb62lhzbcHDu7VKx22mzEpeZBrdiDCuyDqyw5HlPNq2Zs-X8KAkFzLbpPJG_qCqtZsVicZmoQC1QRoQW", $data, FCM_SERVER_KEY2, 'ANDROID');
                            pushTimeUpdate();
                            echo $result;

                        } catch (Exception $e) {
                            echo $e;
                        }

                    }
                    else{

                    }

                    echo $result;

                } else {//그 외
                    //status = 1 으로 바꿔주기 (앉았다고 바꾸기)
                    postSeat($seatNo, $status);

                }
            } else if ($seatNo == 1 && $status == 0) { //1번 좌석에서 사람이 일어난경우
                if ($seat1 == 2) {//임산부가 앉아있는 경우
                    //status = 0 으로 바꿔주기 (빈좌석으로 바꾸기)
                    postSeat($seatNo, $status);

                } else if ($seat1 == 3) { //예약된 좌석의 경우
                    soundOff($seatNo);
                    //소리꺼주기 (예약된좌석에 다른사람이 앉아있다가 일어난것이기 때문)
                } else {
                    //status = 0 으로 바꿔주기 (빈좌석으로 바꾸기)
                    postSeat($seatNo, $status);
                }
            } else if ($seatNo == 2 && $status == 1) { // 2번좌석에 사람이 앉은경우
                if ($seat2 == 3) { // 예약된 좌석일경우
                    //폰에게 fcm전송해서 확인받기, 임산부가 맞으면 status바꿔주고 알림끄기, 아니면 그냥두기
                    $timeDiff = pushTimeCheck();
//                    echo $timeDiff;
                    if($timeDiff >3){
                        $data = array("title" => "Love Baby",
                            "body" => "예약된 좌석에 사람이 감지되어있습니다.",
                            "message" => "예약한 좌석에 앉으셨나요?",
                            "seatNo" => $seatNo);

                        try {
//                            $result = sendFcm("c2umRG18drQ:APA91bE6GSFj--mXUJthwtm9cpH9wiLx9IOLSmzorKCjpfeRupFgigGQtmugIqZhNeC3iyVUWg8l735ECO06cQWitlhSU691PQRxJlJXMuv0DCqUykJUPAfzLJLLax9XQM4UJcWsUfHn", $data, FCM_SERVER_KEY2, 'ANDROID');
                            $result = sendFcm("f38ifyZxI0U:APA91bGADmUNfZm255OGYGxJr_1X-bphsa0KL_XTaDyj7INEy5WF5fifQY3IYb62lhzbcHDu7VKx22mzEpeZBrdiDCuyDqyw5HlPNq2Zs-X8KAkFzLbpPJG_qCqtZsVicZmoQC1QRoQW", $data, FCM_SERVER_KEY2, 'ANDROID');
                            pushTimeUpdate();
                            echo $result;

                        } catch (Exception $e) {
                            echo $e;
                        }

                    }
                    else{

                    }
                } else {//그 외
                    //status = 1 으로 바꿔주기 (앉았다고 바꾸기)
                    postSeat($seatNo, $status);
                }
            } else if ($seatNo == 2 && $status == 0) { //2번 좌석에서 사람이 일어난경우
                if ($seat2 == 2) {//임산부가 앉아있는 경우
                    //status = 0 으로 바꿔주기 (빈좌석으로 바꾸기)
                    postSeat($seatNo, $status);
                } else if ($seat2 == 3) { //예약된 좌석의 경우
                    soundOff($seatNo);
                    //소리꺼주기 (예약된좌석에 다른사람이 앉아있다가 일어난것이기 때문)

                } else {
                    //status = 0 으로 바꿔주기 (빈좌석으로 바꾸기)
                    postSeat($seatNo, $status);
                }
            }

            break;

        case "signUp":
            $id = $req->id;
            $password = $req->password;
            $url = $req->url;
            $name = $req->name;

            $res->code = 100;
            $res->message = "성공";

            $result = signUpRb($id, $password, $url, $name);

            if ($result == 1) {
                $res->code = 100;
                $res->message = "성공";
            } else {
                $res->code = 200;
                $res->message = "실패";
            }

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "getUser":
            $result = getUserLb();

            if ($result == 999) {
                $res->code = 200;
                $res->message = "실패";
            } else {
                $res->code = 100;
                $res->result = $result;
            }

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "verifyUser":
            $userNo = $req->userNo;

            $result = verifyUser($userNo);

            if ($result == 999) {
                $res->code = 200;
                $res->message = "실패";
            } else {
                $res->code = 100;
                $res->message = "성공";
            }

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "reservation":
            $seatNo = $req->seatNo;

            $result = reservationDone($seatNo);

            if ($result == 999) {
                $res->code = 200;
                $res->message = "실패";
            } else {
                $res->code = 100;
                $res->message = "성공";
            }

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;


        case "getUserDetail":
//            $id = $req->userId;
            $id = $vars["userNo"];


            $result = getUserLbDetail($id);

            if ($result == 999) {
                $res->code = 200;
                $res->message = "실패";
            } else {
                $res->code = 100;
                $res->result = $result;
            }

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "reservationRequest":
//            $id = $req->userId;
            $seatNo = $req->seatNo;


            $result = reservationRequest($seatNo);

            if ($result == 999) {
                $res->code = 200;
                $res->message = "실패";
            } else {
                $res->code = 100;
                $res->message = "성공";
            }

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "soundRequest":
            $seatNo = $req->seatNo;

            $result = soundRequest($seatNo);

            if ($result == 999) {
                $res->code = 200;
                $res->message = "실패";
            } else {
                $res->code = 100;
                $res->message = "성공";
            }

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

        case "isVerify":
//            $id = $req->userId;
            $id = $req->id;


            $result = getUserVerify($id);

            if ($result) {
                $res->code = 100;;
                $res->isVerify = true;
            } else {
                $res->code = 100;
//                $res->result = $result;
                $res->isVerify = false;
            }

            echo json_encode($res, JSON_NUMERIC_CHECK);

            break;

    }
} catch (\Exception $e) {
    return getSQLErrorException($errorLogs, $e, $req);
}

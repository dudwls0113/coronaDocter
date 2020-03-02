<?php
require 'function.php';

const JWT_SECRET_KEY = "TEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEY";

$res = (Object)Array();
header('Content-Type: json');
$req = json_decode(file_get_contents("php://input"));
try {
    addAccessLogs($accessLogs, $req);
    switch ($handler) {
        /*
         * API No. 0
         * API Name : JWT 유효성 검사 테스트 API
         * 마지막 수정 날짜 : 19.04.25
         */
        case "user":

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
            else if($isintval === 1)
            {
                http_response_code(200);
                $res->result = getUser($userNo);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "유저정보 조회를 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "detailUser": // 유저상세정보 조회 API

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
            else if($isintval === 1)
            {
                http_response_code(200);
                $result = viewdetailUser($userNo);
                $res->result->user = $result['user'];
                $res->result->national =$result['national'];
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "유저상세정보 조회를 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "patchUser": //유저상세정보 수정 api

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

            $count = 0;
            $nationalList= $req->result;
            foreach($nationalList as $nationalNo => $value)
            {
                $nationalId = $value->nationalNo;
                $national[$count++] = $nationalId;

            }   
            $url = $req->url;
            $introduce = $req->introduce;

            $patchResult = patchUser($url, $introduce, $userNo, $national);

            if($patchResult == 1)
            {
                http_response_code(200);
//                $res->result = patchUser($userNo);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "유저상세정보 수정을 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "myArea": //관심지역 조회 api

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
            else if($isintval === 1)
            {
                http_response_code(200);
                $res->result = getMyarea($userNo);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "관심지역 조회를 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "postScrap":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $scrapName = $req->name;
            $closure = $req->closure;
            $photo = $req->photo;

            $count = 0;
            foreach ($photo as $url => $value) {
                $urlResult = $value->url;
                $photoResult[$count++] = $urlResult;
            }

            $userNo = convert_to_userNo($email);


            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (count($photo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 125;
                    $res->message = "사진 URL을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($scrapName) < 1) {
                    $res->isSuccess = false;
                    $res->code = 126;
                    $res->message = "스크랩북 이름을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($closure) < 1) {
                    $res->isSuccess = false;
                    $res->code = 127;
                    $res->message = "스크랩 공개여부를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (count($photo) > 0 and strlen($scrapName) > 0 and strlen($closure) > 0) {
                    http_response_code(200);
                    postScrap($photoResult, $scrapName, $closure, $userNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "스크랩북 추가를 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

            }
            break;

        case "getScrap":

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
            } else if ($isintval === 1) {

                $getUserScrap = getScrap($userNo);
                $is_exist_scrap = scrapCheck2($userNo);

                if($is_exist_scrap == 0)
                {
                    $res->isSuccess = TRUE;
                    $res->code = 202;
                    $res->message = "스크랩북 없음";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if ($getUserScrap == false) {
                    $res->isSuccess = False;
                    $res->code = 200;
                    $res->message = "스크랩북 목록 조회 실패";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;

                } else {
                    $res->result = $getUserScrap;
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "스크랩북 목록 조회 성공";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }
            }

            break;

        case "patchScrap":


            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $scrapNo = $vars["scrapNo"];
            $scrapName = $req->name;
            $closure = $req->closure;
            $photo = $req->photo;


            $count = 0;
            foreach ($photo as $url => $value) {
                $urlResult = $value->url;
                $photoResult[$count++] = $urlResult;
            }

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($scrapName) < 1) {
                    $res->isSuccess = false;
                    $res->code = 134;
                    $res->message = "스크랩 이름을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($closure) < 1) {
                    $res->isSuccess = false;
                    $res->code = 135;
                    $res->message = "공개여부를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (count($photoResult) < 1) {
                    $res->isSuccess = false;
                    $res->code = 136;
                    $res->message = "사진 URL을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isexistScrap = scrapCheck($userNo, $scrapNo);

                if ($isexistScrap == 0) {
                    $res->isSuccess = false;
                    $res->code = 137;
                    $res->message = "유효한 스크랩북 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                patchScrap($userNo, $scrapNo, $closure, $scrapName,$photoResult);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "스크랩북 수정을 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
            }

            break;


        case "getMyPost":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $page = (int)$vars["page"];

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                $getMyPost = getUserFeed($userNo, $page);
                $is_check_Fesd = is_check_Fesd($userNo);

                if($is_check_Fesd == 0)
                {
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

        case "getMyGallery":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $page = (int)$vars["page"];

            $userNo = convert_to_userNo($email);

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

                if($is_check_gallery == 0)
                {
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

        case "deleteScrap":
            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];
            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $scrapNo = $vars["scrapNo"];

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
            else if($isintval === 1)
            {

                if(strlen($scrapNo) < 1)
                {
                    $res->isSuccess = FALSE;
                    $res->code = 150;
                    $res->message = "스크랩 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isexistScrap = scrapCheck($userNo, $scrapNo);

                if ($isexistScrap == 0) {
                    $res->isSuccess = false;
                    $res->code = 137;
                    $res->message = "유효한 스크랩북 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                deletedScrap($userNo, $scrapNo);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "스크랣북 삭제를 성공";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "validateJwt":
            // jwt 유효성 검사
            if (!isValidHeader($jwt, JWT_SECRET_KEY)) {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req);
                return;
            }

            http_response_code(200);
            $res->isSuccess = TRUE;
            $res->code = 100;
            $res->message = "테스트 성공";

            echo json_encode($res, JSON_NUMERIC_CHECK);
            break;
        /*
         * API No. 0
         * API Name : JWT 생성 테스트 API
         * 마지막 수정 날짜 : 19.04.25
         */
    }
} catch (\Exception $e) {
    return getSQLErrorException($errorLogs, $e, $req);
}

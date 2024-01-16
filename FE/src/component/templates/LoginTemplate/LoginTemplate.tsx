import styled from "./style";
import {
  bg01,
  bg02,
  bg03,
  bg04,
  bg05,
  bg06,
  bg07,
  bg08,
  bg09,
  bg10,
  IconGoogle,
  IconKakao,
  IconNaver,
  IconUwhoo,
} from "@res/index";
import HWTypography from "@src/component/atoms/HWTypography/HWTypography";
import {useCommon} from "@src/providers/CommonProvider";
import {useEffect} from "react";
import Login from "@src/component/organisms/LoginGrid/Login/Login";
const LoginTemplate = () => {
  const common = useCommon();

  useEffect(() => {
    console.log("loginTemplate", common.userInfo)
  },[])

  return (
    <section
      className="login-template-wrapper"
      css={styled.wrapper([bg01, bg02, bg03, bg04, bg05, bg06, bg07, bg08, bg09, bg10])}
    >
      <div css={styled.loginWrapper}>
        <IconUwhoo width={"300px"} height={"48px"} />
        <div css={styled.typo1}>로그인</div>
        <div css={styled.typo2}>
          간편 로그인으로&nbsp;
          <HWTypography variant={"bodyXL"} color={"#6D6ADA"}>
            유후
          </HWTypography>
          의 다양한&nbsp;
          <HWTypography variant={"bodyXL"} color={"#C7C8D3"}>
            맞춤 추천 서비스
          </HWTypography>
          를 이용해보세요!
        </div>
        <Login />
      </div>
    </section>
  );
};

export default LoginTemplate;
import styled from "./style";
import MenuAccordion from "@src/component/atoms/MenuAccordion/MenuAccordion";
import { useEffect, useState } from "react";
import HWTypography from "@src/component/atoms/HWTypography/HWTypography";
import HWAvatar from "@src/component/atoms/HWAvatar/HWAvatar";
import { IconApple, IconDisney, IconNetflix, IconWatcha, IconWavve } from "@res/index";
import { PLATFORM_ID_NAME, PLATFORM_NAME } from "@src/variables/CommonConstants";
import { PlatformConstants } from "@src/variables/PlatformConstants";

const AccordionSubscribe = ({ ott }: { ott: number[] }) => {
  const [open, setOpen] = useState(true);
  const [subscribe, setSubscribe] = useState<number[]>(ott);

  useEffect(() => {
    console.log(subscribe);
  }, [subscribe]);

  return (
    <MenuAccordion
      title={
        <div css={styled.title}>
          <HWTypography variant={"headlineS"} family={"Pretendard-SemiBold"} color={"#ffffff"}>
            구독 OTT
          </HWTypography>
          <HWTypography variant={"headlineXXS"} family={"Pretendard"} color={"#D9DAE5"}>
            내가 구독하는 OTT를 표시해 주세요. 구독하고 있는 OTT 작품만 볼 수 있는 필터를 사용할 수
            있어요!
          </HWTypography>
        </div>
      }
      isExpanded={open}
      switchExpanded={() => setOpen(!open)}
      customCss={styled.accordion}
    >
      <div css={styled.subWrapper}>
        {Object.values(PlatformConstants).map((v: any) => (
          <HWAvatar
            key={v.id}
            size={"80px"}
            customCss={styled.avatar}
            active={subscribe.includes(v.id)}
            onClick={() => {
              if (subscribe.includes(v.id)) {
                setSubscribe(subscribe.filter((s) => s !== v.id));
              } else {
                setSubscribe((prev) => [...prev, v.id]);
              }
            }}
          >
            <v.icon />
          </HWAvatar>
        ))}
      </div>
    </MenuAccordion>
  );
};

export default AccordionSubscribe;

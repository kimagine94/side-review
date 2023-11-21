import styled from "./style";
import HWTypography from "@src/component/atoms/HWTypography/HWTypography";
import Color from "@src/common/styles/Color";
import HWChip from "@src/component/atoms/HWChip/HWChip";
import HWButton from "@src/component/atoms/HWButton/HWButton";
import { IconInit } from "@res/index";
import CenterWrapper from "@src/component/atoms/CenterWrapper/CenterWrapper";
import { useCommon } from "@src/providers/CommonProvider";
import { useSearchParams } from "react-router-dom";
import { GENRE_NAME, PLATFORM_NAME } from "@src/variables/CommonConstants";

const SearchResultHeader = ({ data }: any) => {
  const commonContext = useCommon();
  const [searchParams] = useSearchParams();
  const search = searchParams.get("search");
  const paramList = [...searchParams];

  console.log(paramList);
  return (
    <div className={"search-header-wrapper"} css={styled.wrapper}>
      <CenterWrapper>
        <div css={styled.subWrapper}>
          <div css={styled.left}>
            {search ? (
              <>
                <HWTypography variant={"headlineS"} family={"Pretendard-Bold"}>
                  `{search}`
                </HWTypography>
                <HWTypography
                  variant={"headlineS"}
                  family={"Pretendard"}
                  color={Color.dark.grey500}
                >
                  의 검색결과
                </HWTypography>
              </>
            ) : (
              <>
                <HWTypography
                  variant={"headlineS"}
                  family={"Pretendard"}
                  color={Color.dark.grey500}
                >
                  필터 검색결과
                </HWTypography>
                <HWTypography
                  variant={"headlineS"}
                  family={"Poppins"}
                  color={Color.dark.primary700}
                  css={styled.typo1}
                >
                  {data.length}
                </HWTypography>
              </>
            )}
          </div>
          <div css={styled.right}>
            {paramList.map(([key, value]: any) => {
              if (key === "genre")
                return value
                  .split(",")
                  .map((v: any) => <HWChip label={GENRE_NAME[v]} customCss={styled.chip} />);
              if (key === "platform")
                return value
                  .split(",")
                  .map((v: any) => <HWChip label={PLATFORM_NAME[v]} customCss={styled.chip} />);
              if (key === "watch")
                return value
                  .split(",")
                  .map((v: any) => <HWChip label={v} customCss={styled.chip} />);
              if (key === "rating")
                return (
                  <HWChip
                    label={value
                      .split(",")
                      .map((v: any) => v + (v ? "점" : ""))
                      .join(" - ")}
                    customCss={styled.chip}
                  />
                );
              if (key === "date")
                return (
                  <HWChip
                    label={value
                      .split(",")
                      .map((v: any) => v + (v ? "년" : ""))
                      .join(" - ")}
                    customCss={styled.chip}
                  />
                );
              if (key === "sort") return <HWChip label={value} customCss={styled.chip} />;
            })}
            {/*<HWChip
              label={"액션"}
              onClick={() => {
                console.log("click");
                if (commonContext.filterRef.genreRef?.current) {
                  commonContext.onHandleFilterOpen(true);
                  commonContext.filterRef.genreRef.current.click();
                }
              }}
              customCss={styled.chip}
            />
            <HWChip label={"액션"} customCss={styled.chip} />
            <HWChip label={"액션"} customCss={styled.chip} />
            <HWChip label={"액션"} />*/}
            {/*<HWButton variant={"lowest"}>
              <IconInit />
              <div>초기화</div>
            </HWButton>*/}
          </div>
        </div>
      </CenterWrapper>
    </div>
  );
};

export default SearchResultHeader;
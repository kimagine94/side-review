import styled from "./style";
import WrapperTitle from "@src/component/atoms/WrapperTitle/WrapperTitle";
import CenterWrapper from "@src/component/atoms/CenterWrapper/CenterWrapper";
import { useState } from "react";
import HWToggleButton from "@src/component/atoms/HWToggleButton/HWToggleButton";
import HWToggleButtonGroup from "@src/component/atoms/HWToggleButtonGroup/HWToggleButtonGroup";
import CardList from "@src/component/molecules/CardList/CardList";
import ContentCard from "@src/component/atoms/ContentCard/ContentCard";
import PersonCardList from "@src/component/molecules/PersonCardList/PersonCardList";
import HWTypography from "@src/component/atoms/HWTypography/HWTypography";
import Color from "@src/common/styles/Color";
import PersonCard from "@src/component/atoms/PersonCard/PersonCard";
import person1 from "@res/temp/person1.png";
import {card1, card3, IconChevronDoubleDown} from "@res/index";
const SearchResultContent = () => {
  const [contentsList, setContentsList] = useState([
    <ContentCard src={card3} />,
    <ContentCard src={card3} />,
    <ContentCard src={card3} />,
    <ContentCard src={card3} />,
    <ContentCard src={card3} />,
    <ContentCard src={card3} />,
  ]);
  const [toggle1, setToggle1] = useState<string>("a");
  const [toggle2, setToggle2] = useState<string>("a");

  const props1 = (value: string) => {
    return {
      checked: toggle1 === value,
      onClick: () => {
        setToggle1(value);
      },
    };
  };
  const props2 = (value: string) => {
    return {
      checked: toggle2 === value,
      onClick: () => {
        setToggle2(value);
      },
    };
  };

  return (
    <div className={"search-content-wrapper"} css={styled.wrapper}>
      <CenterWrapper>
        <WrapperTitle title={"일치하는 검색어"} subTitle={"14"} customCss={styled.subTitle} />
        <HWToggleButtonGroup customCss={styled.toggle}>
          <HWToggleButton {...props1("a")}>드라마</HWToggleButton>
          <HWToggleButton {...props1("b")}>인물</HWToggleButton>
        </HWToggleButtonGroup>
        <div css={styled.subWrapper}>
          <>
            {toggle1 === "a" && (
              <>
                <div css={styled.sub1}>{contentsList.map((v) => v)}</div>
                <div css={styled.plusBtn}>
                  <HWTypography
                    variant={"headlineXXS"}
                    family={"Pretendard-SemiBold"}
                    color={Color.dark.primary800}
                    onClick={() => {
                      setContentsList((prev) => [
                        ...prev,
                        <ContentCard src={card3} />,
                        <ContentCard src={""} />,
                        <ContentCard src={""} />,
                        <ContentCard src={card3} />,
                        <ContentCard src={""} />,
                        <ContentCard src={""} />,
                      ]);
                    }}
                  >
                    더보기
                  </HWTypography>
                  <IconChevronDoubleDown />
                </div>
              </>
            )}
            {toggle1 === "b" && (
              <>
                <div css={styled.sub2}>
                  <PersonCard width="97px" height={"97px"} src={person1} className={"image-card"} onClick={() => {}} />
                  <PersonCard width="97px" height={"97px"} src={person1} className={"image-card"} onClick={() => {}} />
                  <PersonCard width="97px" height={"97px"} src={person1} className={"image-card"} onClick={() => {}} />
                  <PersonCard width="97px" height={"97px"} src={person1} className={"image-card"} onClick={() => {}} />
                  <PersonCard width="97px" height={"97px"} src={person1} className={"image-card"} onClick={() => {}} />
                  <PersonCard width="97px" height={"97px"} src={person1} className={"image-card"} onClick={() => {}} />
                  <PersonCard width="97px" height={"97px"} src={person1} className={"image-card"} onClick={() => {}} />
                  <PersonCard width="97px" height={"97px"} src={person1} className={"image-card"} onClick={() => {}} />

                </div>
                <HWTypography
                  variant={"headlineXXS"}
                  family={"Pretendard-SemiBold"}
                  color={Color.dark.primary800}
                  onClick={() => {
                    setContentsList((prev) => [
                      ...prev,
                      <ContentCard src={""} />,
                      <ContentCard src={""} />,
                      <ContentCard src={""} />,
                    ]);
                  }}
                >
                  더보기
                </HWTypography>
              </>
            )}
          </>
        </div>
      </CenterWrapper>
      <CenterWrapper>
        <WrapperTitle title={"연관 검색어"} subTitle={"24"} customCss={styled.subTitle}/>
        <HWToggleButtonGroup customCss={styled.toggle}>
          <HWToggleButton {...props2("a")}>드라마</HWToggleButton>
          <HWToggleButton {...props2("b")}>인물</HWToggleButton>
        </HWToggleButtonGroup>
        <div css={styled.subWrapper}>
          <>
            {toggle2 === "a" && (
              <div css={styled.sub1}>
                <ContentCard src={""} />
                <ContentCard src={""} />
                <ContentCard src={""} />
                <ContentCard src={""} />
                <ContentCard src={""} />
                <ContentCard src={""} />
                <ContentCard src={""} />
              </div>
            )}
            {toggle2 === "b" && (
              <div css={styled.sub2}>
                <PersonCard src={""} className={"image-card"} onClick={() => {}} />
                <PersonCard src={""} className={"image-card"} onClick={() => {}} />
                <PersonCard src={""} className={"image-card"} onClick={() => {}} />
                <PersonCard src={""} className={"image-card"} onClick={() => {}} />
              </div>
            )}
          </>
        </div>
      </CenterWrapper>
    </div>
  );
};

export default SearchResultContent;

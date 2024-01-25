import { forwardRef, useCallback, useEffect, useRef, useState } from "react";
import styled from "./style";
import ContentCard from "@src/component/atoms/ContentCard/ContentCard";
import HWTypography from "@src/component/atoms/HWTypography/HWTypography";
import CenterWrapper from "@src/component/atoms/CenterWrapper/CenterWrapper";
import PreviewBoxVertical from "@src/component/molecules/PreviewBoxVertical/PreviewBoxVertical";
import { VirtuosoGrid } from "react-virtuoso";
import { ContentDO } from "@src/interfaces/api.interface";
import { UWAxios } from "@src/common/axios/AxiosConfig";
import { CONTENTS_TABS } from "@src/variables/APIConstants";
import { useMutation } from "@tanstack/react-query";

const UpcomingContent = ({ data }: any) => {
  const mainRef = useRef<HTMLDivElement | null>(null);
  const [open, setOpen] = useState<boolean>(false);
  const [selectedCard, setSelectedCard] = useState<ContentDO | null>(null);
  const [selectedCardIdx, setSelectedCardIdx] = useState<number | null>(null);
  const [popularList, setPopularList] = useState<any>(data);
  const virtuosoRef = useRef<any>();
  const [isScrolling, setIsScrolling] = useState(false);

  const mutation = useMutation({
    mutationFn: async ({ p }: any) => {
      return await UWAxios.contents.getContents({
        tab: CONTENTS_TABS.POPULARITY,
        pagination: p,
      });
    },
    onSuccess: (data: any) => {
      setPopularList((prev: any) => [...prev, ...data]);
    },
  });

  return (
    <div className="upcoming-content-wrapper" css={styled.wrapper} ref={mainRef}>
      <CenterWrapper customCss={styled.centerWrapper}>
        <div css={styled.header}>
          <div css={styled.headline}>
            <HWTypography variant={"headlineM"} family={"Pretendard-SemiBold"}>
              UWHOO 공개 예정 작품
            </HWTypography>
          </div>
          <div css={styled.subHeadline}>
            <HWTypography variant={"headlineXS"} family={"Pretendard"}>
              💡
            </HWTypography>
          </div>
        </div>
        <div css={styled.contentWrapper}>
          {selectedCard && (
            <div className={`side-preview-wrapper ${selectedCard && "open"}`} css={styled.leftBox}>
              <div className={`floating-box ${selectedCard && "open"}`} css={styled.floatWrapper}>
                <PreviewBoxVertical item={selectedCard} />
              </div>
            </div>
          )}
          <div className={`popular-cards-wrapper`} css={styled.rightBox}>
            <VirtuosoGrid
              ref={virtuosoRef}
              data={popularList}
              endReached={(index) => {
                mutation.mutate({ p: popularList.length })
              }}
              useWindowScroll={true}
              components={{
                List: forwardRef((props, ref) => (
                  <div {...props} css={styled.listContainer(!!selectedCard)} ref={ref} />
                )),
                Item: (props) => (
                  <div
                    {...props}
                    className={"item-container"}
                    css={styled.itemContainer(!!selectedCard)}
                  />
                ),
              }}
              isScrolling={setIsScrolling}
              itemContent={(i, v) => {
                return (
                  <div
                    className={"content-slide"}
                    key={v.id}
                    onClick={() => {
                      if (selectedCard === null) {
                        setOpen(true);
                        setSelectedCard(v);
                        setSelectedCardIdx(i);
                      } else {
                        if (selectedCard.id === v.id) {
                          setOpen(false);
                          setSelectedCard(null);
                          setSelectedCardIdx(null);
                        } else {
                          setSelectedCard(v);
                          setSelectedCardIdx(i);
                        }
                      }
                    }}
                    css={styled.item}
                  >
                    <ContentCard
                      id={v.id}
                      key={v.id}
                      className={`image-card`}
                      srcId={v.poster}
                      rank={i + 1}
                      contentName={v.name}
                      platform={v.platform}
                      age={v.age}
                      date={v.year}
                      season={v.season}
                      rating={v.rating}
                      active={selectedCard ? selectedCard?.id === v.id : true}
                      customCss={styled.card}
                    />
                    {/*                    {isScrolling ? (
                      <ContentEmptyCard active={selectedCard ? selectedCard?.id === v.id : true} />
                    ) : (
                      <ContentCard
                        key={v.id}
                        className={`image-card`}
                        srcId={v.poster}
                        rank={i + 1}
                        contentName={v.name}
                        platform={v.platform}
                        age={v.age}
                        year={v.year}
                        rating={v.rating}
                        active={selectedCard ? selectedCard?.id === v.id : true}
                        customCss={styled.card}
                      />
                    )}*/}
                  </div>
                );
              }}
            />
          </div>
        </div>
      </CenterWrapper>
    </div>
  );
};

export default UpcomingContent;
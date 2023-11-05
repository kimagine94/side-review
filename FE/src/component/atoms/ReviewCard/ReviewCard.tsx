import styled from "./style";
import Color from "@src/common/styles/Color";
import { SerializedStyles } from "@emotion/react";
import { IconThumbDown, IconThumbUp } from "@res/index";
import HWChip from "@src/component/atoms/HWChip/HWChip";
import Divider from "@src/component/atoms/Divider/Divider";

interface ReviewCardProps {
  width?: string;
  height?: string;
  radius?: string;
  backgroundColor?: string;
  customCss?: SerializedStyles;
  children?: React.ReactNode;
  best?: boolean;
  spoiler?: boolean;
  footer?: boolean;
  date?: string;
  line?: number;
  onClick?: () => void;
}

const ReviewCard = ({
  width = "420px",
  height = "140px",
  radius = "10px",
  backgroundColor = Color.dark.elevation16,
  best = true,
  spoiler = false,
  footer = false,
  date,
  line,
  customCss,
  children,
  onClick,
}: ReviewCardProps) => {
  return (
    <div css={[styled.wrapper(width, height, radius, backgroundColor), customCss]} onClick={onClick}>
      <div css={styled.topWrapper}>
        <div css={styled.chipWrapper}>
          {best && (
            <HWChip
              color={"best"}
              label={
                <div css={styled.bestDiv}>
                  <IconThumbUp />
                  Best Review
                </div>
              }
            />
          )}
          {spoiler && <HWChip color={"spoiler"} label={"스포일러"} css={styled.spoiler} />}
        </div>
        <div css={styled.dateDiv}>{date}</div>
      </div>
      <div css={[styled.contents, line && styled.lineClamp(line)]}>{children}</div>
      {footer && (
        <>
          <Divider />
          <div css={styled.bottomWrapper} onClick={(e) => {
            e.stopPropagation( );
          }}>
            <div css={styled.flex1}>
              <div css={styled.flex2}>
                <IconThumbUp />
                100
              </div>
              <div css={styled.flex2}>
                <IconThumbDown />
                20
              </div>
            </div>
            <div css={styled.flex1} >
              <IconThumbUp />
              <IconThumbDown />
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default ReviewCard;

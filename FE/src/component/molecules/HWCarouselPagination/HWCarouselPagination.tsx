import React from "react";
import { useMemo } from "react";
import { getDotStyle } from "./utils";
import styled from "./style";
import { css } from "@emotion/react";
import { HWCarouselPaginationProps } from "./type";
import HWCircle from "@src/component/atoms/HWCircle/HWCircle";

const HWCarouselPagination = ({
  className,
  customCss,
  maxPage,
  curPage,
  activeColor = "#6D6ADA",
  inactiveColor = "#42424A",
  size = 12,
  gap = 16,
  onClickCircle,
}: HWCarouselPaginationProps) => {
  const list = useMemo(() => [...new Array(maxPage)], [maxPage]);
  const classNames = useMemo(
    () => ["HW-Carousel-Pagination", className ? className : null].filter(Boolean).join(" "),
    [className]
  );

  return (
    <div className={classNames} css={[styled.wrapper(curPage, size, gap), customCss]}>
      <div className="HW-Carousel-Pagination-Contents-Wrapper relative transition-transform ease-out duration-400">
        {list.map((_, i) => {
          const dotStyle = getDotStyle({ idx: i + 1, curPage, maxPage });

          const handleClickCircle = (e: React.MouseEvent<HTMLDivElement>) => {
            if (onClickCircle) {
              onClickCircle(i + 1, e);
            }
          };
          return (
            <div tabIndex={dotStyle.size === 0 ? -1 : 0} key={i}>
              <HWCircle
                className={"icon-circle transition-all transition-hover-none ease-out duration-400"}
                color={i === curPage ? activeColor : inactiveColor}
                border={i === curPage ? "none" : "1px solid #84838d"}
                customCss={css`
                  ${dotStyle}
                `}
                onClick={handleClickCircle}
              />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default HWCarouselPagination;

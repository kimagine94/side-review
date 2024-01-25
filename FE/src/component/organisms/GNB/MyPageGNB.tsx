import styled from "./style";
import { NavLink, useLocation, useNavigate } from "react-router-dom";
import HWButton from "@src/component/atoms/HWButton/HWButton";
import { IconAlarm, IconSearch, IconUwhoo } from "@res/index";
import HWIconButton from "@src/component/atoms/HWIconButton/HWIconButton";
import { useEffect, useRef, useState } from "react";
import SearchBar from "@src/component/molecules/SearchBar/SearchBar";
import CenterWrapper from "@src/component/atoms/CenterWrapper/CenterWrapper";
import { useCommon } from "@src/providers/CommonProvider";
import { Box, ClickAwayListener } from "@mui/material";
import ProfileBox from "@src/component/molecules/ProfileBox/ProfileBox";

const MyPageGNB = (props: { children?: React.ReactNode }) => {

  const navigate = useNavigate();
  return (
    <>
      <header css={styled.wrapper("#252525", true)}>
        <div css={styled.subWrapper}>
          <div css={styled.leftGroups}>
            <div>
              <IconUwhoo css={styled.logo} onClick={() => navigate("/")} />
            </div>
          </div>
          <div css={styled.centerGroups}></div>
          <div css={styled.rightGroups}>
            <HWButton variant={"primary"} size={"small"} onClick={() => {}}>
              저장
            </HWButton>
          </div>
        </div>
      </header>
    </>
  );
};
export default MyPageGNB;

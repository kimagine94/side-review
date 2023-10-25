import { css } from "@emotion/react";

export default {
  wrapper: (url: string) => css`
    position: relative;
    width: 100%;
    height: 600px;
    background-image: url(${url});
    background-size: cover;

    //opacity: 0.5;
    ::before {
      position: absolute;
      content: "";

      background: linear-gradient(0deg, rgba(0, 0, 0) 5%, rgba(0, 0, 0, 0.5) 100%);

      //background-blend-mode: multiply;
      //background: linear-gradient(0deg, #000 20%, rgba(0, 0, 0, 0) 100%);
      //linearGradient: 0deg, #000 20%, rgba(0, 0, 0, 0) 100%);
      //background: url(${url}), linear-gradient(0deg, #000 50%, #000 100%);
      //opacity: 0.5;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
    }
  `,
  centerWrapper: css`
    height: 100%;
  `,
  subWrapper: css`
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    gap: 78px;
    //justify-content: space-between;
    //z-index: 1;
  `,
  rightBox: css`
    .grid {
      row-gap: 8px;
    }
  `,
  avatarGroup: css`
    //display: flex;
    //flex-direction: unset;
    justify-content: start;
    div {
      border: none !important;
      width: 60px;
      height: 60px;
      font-size: 60px;
      background-color: transparent;
      border: none !important;
    }
  `,
  avatar: css``,
  avatarWrapper: css`
    position: absolute;
    bottom: 30px;
    right: 0;
  `,
  inputGroups: css`
    display: flex;
    justify-content: space-between;
  `,
  ratingGroups: css`
    display: flex;
    gap: 70px;
  `,
  btnGroups: css`
    display: flex;
    gap: 16px;
  `,
  ratingBox: css`
    display: flex;
    flex-direction: column;
  `,
  rating: css`
    font-size: 30px;
    svg {
      width: 1em;
      height: 1em;
      flex-shrink: 0;
    }
  `,
};

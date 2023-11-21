import { css } from "@emotion/react";

export default {
  wrapper: (src: string, width: string, height: string) => css`
    background-image: url(${src});
    width: ${width};
    height: ${height};
    position: relative;
    background-size: contain;
    background-repeat: no-repeat;
  `,
  playIcon: css`
    position: absolute;
    right: 20px;
    bottom: 16px;
  `,
  iframe: css`
    width: 800px;
    height: 450px;
    border: none;
  `
};
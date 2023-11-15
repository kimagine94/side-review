import styled from "./style";
import PopularContent from "@src/component/organisms/PopularGrid/Contents/PopularContent";
import { UWAxios } from "@src/common/axios/AxiosConfig";
import { useQuery } from "@tanstack/react-query";

const PopularTemplate = () => {
  const { status, data, error } = useQuery({
    queryKey: ["main", "list"],
    queryFn: async () => await UWAxios.sample.getSample(),
    refetchOnWindowFocus: false,
  });

  return (
    <>
      <div className="popular-template-wrapper" css={styled.wrapper}>
        <div css={styled.contents}>
          {status === "success" && (
            <PopularContent data={data} />
          )}
        </div>
      </div>
    </>
  );
};
export default PopularTemplate;
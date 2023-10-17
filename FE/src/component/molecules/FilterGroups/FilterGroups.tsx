import { useState } from "react";
import styled from "./style";
import HWOutlinedSelectBox from "@src/component/atoms/HWOutlinedSelectBox";
import { SelectChangeEvent } from "@mui/material";
import HWChip from "@src/component/atoms/HWChip/HWChip";

const FilterGroups = () => {
  const [value, setValue] = useState<string[]>([]);
  const handleChange = (e: SelectChangeEvent<string[]>) => {
    const { value } = e.target;
    setValue(typeof value === "string" ? value.split(",") : [...value]);
  };
  return (
    <div css={styled.wrapper}>
      <HWOutlinedSelectBox
        multiple
        placeholder={"Option"}
        value={value}
        onChange={handleChange}
        renderValue={(values: any) => (
          <div css={styled.multiBox}>
            {values.map((v: any) => (
              <HWChip key={v} label={v} css={styled.chip} />
            ))}
          </div>
        )}
      >
        {[...Array(7)].map((_, idx) => (
          <HWOutlinedSelectBox.Item key={idx} value={`value${idx}`}>
            {`value${idx}`}
          </HWOutlinedSelectBox.Item>
        ))}
      </HWOutlinedSelectBox>
    </div>
  );
};

export default FilterGroups;

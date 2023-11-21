import { axiosBaseInstance } from "@src/common/axios/AxiosInstance";
import { ContentsDO } from "@src/interfaces/api.interface";

export const CODE_AXIOS = {};

export const UWAxios = {
  sample: {
    async getSample(data: any) {
      const res = await axiosBaseInstance.post<any>(`contents`, data);
      return res.data;
    },
  },
  discover: {
    async getTvList() {
      const res = await axiosBaseInstance.get(
        `discover/tv?watch_region=KR&sort_by=popularity.desc`
      );

      return res.data;
    },
  },
  trending: {
    async getTvList() {
      const res = await axiosBaseInstance.get(`trending/tv/week?language=ko`);

      return res.data;
    },
  },
};
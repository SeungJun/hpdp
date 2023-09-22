import { AxiosResponse } from "axios";
import { companiesApi } from ".";
import * as Interfaces from "../interface/apiDataInterface";

const api = companiesApi;

export async function getCompaniesInfo(
  keyword: string | null,
  accessToken: string | null,
  success: (
    res: AxiosResponse<any, any>
  ) =>
    | AxiosResponse<any, any>
    | PromiseLike<AxiosResponse<any, any>>
    | null
    | undefined
    | void,
  fail: (err: any) => PromiseLike<never> | null | undefined | void
) {
  api.defaults.headers["accessToken"] = `Bearer ${accessToken}`;

  await api.get(`?keyword=${keyword}`).then(success).catch(fail);
}
import style from "../../styles/css/MyWallet.module.css";
import { Icon } from "@iconify/react";
import DefaultButton from "../common/DefaultButton";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { accountActions } from "../../store/account-slice";
import { unregisterAccount } from "../../api/banks";
import { useEffect, useState } from "react";
import { getPoint } from "../../api/points";

const MyWallet = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // const userPoint = useSelector((state: any) => state.user.info.point);
  const accessToken = useSelector((state: any) => state.user.auth.accessToken);
  const isRegistered = useSelector((state: any) => state.account.isRegistered);

  const [userPoint, getUserPoint] = useState(0);

  useEffect(() => {
    getPoint(
      accessToken,
      (res) => {
        console.log(res.data.data);
        getUserPoint(res.data.data);
      },
      (err) => {
        console.error("API 호출 실패:", err);
      }
    );
  }, []);

  const onClick = (event: any) => {
    const { id } = event.target;
    if (id === "reg") {
      // 계좌 등록 화면 이동
      navigate("/profile/testbank/register");
    } else if (id === "unreg") {
      // 계좌 해제 axios
      unregisterAccount(
        accessToken,
        (res) => {
          console.log("계좌 해제 성공 마월", res);
        },
        (err) => {
          console.log("계좌 해제 실패 마월", err);
        }
      );
      // 계좌 해제 redux
      dispatch(accountActions.unregisterAccount());
    } else if (id === "insert") {
      navigate("/payment");
    }
  };

  return (
    <div className={style.mywallet}>
      <div className={style.wrapper}>
        <p className={style.title}>포인트</p>
        <p className={style.point}>{userPoint} P</p>
      </div>
      <div className={style.buttons}>
        {isRegistered ? (
          <DefaultButton
            id="unreg"
            text="계좌 해제"
            styles={{
              width: "48%",
              height: "3rem",
              borderRadius: "0.5rem",
              fontSize: "1.2rem",
            }}
            onClick={onClick}
          />
        ) : (
          <DefaultButton
            id="reg"
            text="계좌 등록"
            styles={{
              width: "48%",
              height: "3rem",
              borderRadius: "0.5rem",
              fontSize: "1.2rem",
            }}
            onClick={onClick}
          />
        )}
        <DefaultButton
          id="insert"
          text="포인트 충전"
          styles={{
            width: "48%",
            height: "3rem",
            borderRadius: "0.5rem",
            fontSize: "1.2rem",
          }}
          onClick={onClick}
        />
      </div>
    </div>
  );
};

export default MyWallet;

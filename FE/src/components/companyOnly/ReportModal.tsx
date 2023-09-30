import { useSelector } from "react-redux";
import style from "../../styles/css/ReportModal.module.css";
import { registerReport } from "../../api/fundings";
import { useState } from "react";
const ReportModal = ({ cM, fundingId, cC }: any) => {
  const accessToken = useSelector((state: any) => state.user.auth.accessToken);
  const [file, setFile] = useState(null);

  // 보고서 등록하기

  const regReport = () => {
    const data = new FormData();
    data.append("fundingId", fundingId);
    if (file) {
      data.append("docs", file);
      registerReport(
        accessToken,
        data,
        (res) => {
          alert("ㅅㄱ");
        },
        (err) => {
          alert("실패.");
        }
      );
    } else {
      alert("파일을 등록해주세요!");
    }
  };
  const fileUploadHandler = (event: any) => {
    const file = event.target.files[0];
    setFile(file);
  };
  const closeModalHandler = (event: any) => {
    event.stopPropagation();
    cC(false);
    cM(false);
    console.log("hi");
  };
  return (
    <div className={style.modal}>
      <div className={style.xbutton}>
        <button onClick={closeModalHandler}>X</button>
      </div>
      <h2>보고서 등록</h2>
      <input type="file" onChange={fileUploadHandler} />
      <button onClick={regReport}>제출하기</button>
    </div>
  );
};

export default ReportModal;
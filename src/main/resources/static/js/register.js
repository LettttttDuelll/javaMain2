function CheckPassWord(password) {
    let rePassword = /^(?![0-9])(?=.*[A-Z]).{8,}$/;
    return rePassword.test(password);
  }
  
  function validateUsername() {
    let username = document.getElementById("username").value.trim();
    let msg = document.getElementById("Message_Username");
    if (username === "  ") {
      msg.innerHTML = "Không được để trống";
      msg.style.color = "red";
      return false;
    }
    if (/\s/.test(username)) {
        msg.innerHTML = "Tên đăng nhập không được chứa khoảng trắng";
        msg.style.color = "red";
        return false;
      }
    msg.innerHTML = "";
    return true;
  }
  
  function validatePassword() {
    let password = document.getElementById("password").value;
    let msg = document.getElementById("Message_Password");
    if (/\s/.test(password)) {
            msg.innerHTML = "Psssword không được chứa khoảng trắng";
            msg.style.color = "red";
            return false;
          }
    if (password.trim() === "  ") {
      msg.innerHTML = "Không được để trống";
      msg.style.color = "red";
      return false;
    }
    if (!CheckPassWord(password)) {
      msg.innerHTML = "Mật khẩu không bắt đầu bằng số và có ít nhất 8 ký tự, trong đó có chữ hoa";
      msg.style.color = "red";
      return false;
    }
    msg.innerHTML = "Mật khẩu hợp lệ";
    msg.style.color = "green";
    return true;
  }
  
  function validateRePassword() {
    let password = document.getElementById("password").value;
    let rePassword = document.getElementById("rePassword").value;
    let msg = document.getElementById("Message_RePassword");
    if (rePassword.trim() === "") {
      msg.innerHTML = "Không được để trống";
      msg.style.color = "red";
      return false;
    }
     if (password !== rePassword) {
      msg.innerHTML = "Mật khẩu nhập lại không khớp";
      msg.style.color = "red";
      return false;
    }
    msg.innerHTML = "Mật khẩu nhập lại hợp lệ";
    msg.style.color = "green";
    return true;
  }
  
  function validateEmail() {
    let email = document.getElementById("email").value;
    let msg = document.getElementById("Message_Email");
    let emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email === "") {
      msg.innerHTML = "Không được để trống";
      msg.style.color = "red";
      return false;
      }
    if (/\s/.test(email)) {
          msg.innerHTML = "Email không hợp lệ vì có khoảng trắng";
          msg.style.color = "red";
          return false;
    }
    if (!emailRegex.test(email)) {
      msg.innerHTML = "Email không hợp lệ";
      msg.style.color = "red";
      return false;
    }
    msg.innerHTML = "Email hợp lệ";
    msg.style.color = "green";
    return true;
  }
  
  
  document.getElementById("username").oninput = validateUsername;
  document.getElementById("password").oninput = function () {
    validatePassword();
    validateRePassword(); 
  };
  document.getElementById("rePassword").oninput = validateRePassword;
  document.getElementById("email").oninput = validateEmail;
  
  document.querySelector("form").onsubmit = function (e) {
    if (
      !validateUsername() ||
      !validatePassword() ||
      !validateRePassword() ||
      !validateEmail()
    ) {
      alert("Vui lòng sửa lỗi trước khi đăng kí");
      e.preventDefault();
      return false;
    }
    alert("Đăng kí thành công");
    return true;
  };
  
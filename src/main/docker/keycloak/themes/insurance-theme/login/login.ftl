<!DOCTYPE html>

<head>
  <meta charset="utf-8">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="robots" content="noindex, nofollow">

  <title>Sign in to JHipster</title>
  <script type="importmap">
    {
        "imports": {
            "rfc4648": "/resources/rk0vz/common/keycloak/vendor/rfc4648/rfc4648.js"
        }
    }
  </script>
  <script src="/resources/rk0vz/login/base/js/menu-button-links.js" type="module"></script>
  <script type="module">
    import { startSessionPolling } from "/resources/rk0vz/login/base/js/authChecker.js";

    startSessionPolling(
      "/realms/jhipster/login-actions/restart?client_id=web_app&tab_id=Z4mqvXRq7N0&client_data=eyJydSI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9sb2dpbi9vYXV0aDIvY29kZS9vaWRjIiwicnQiOiJjb2RlIiwic3QiOiJlaUhBcUJGSm1Yanp6aml4dmw0bHo0alJlUWtyOFdXUnFFdGdPNXNiZDdnPSJ9&skip_logout=true"
    );
  </script>
  <script type="module">
    import { checkAuthSession } from "/resources/rk0vz/login/base/js/authChecker.js";

    checkAuthSession(
      "0/D/GjmGXtb7vutrBk4TxSVr/6od8+9EnoXfhhhpyQs"
    );
  </script>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&display=swap" rel="stylesheet">
<style>
  body {
    padding: 0;
    margin: 0;
    font-family: Arial, Helvetica, sans-serif;
    letter-spacing: 0.8px;
  }

  .btn-primary {
    background-color: #0278f6;
    border-color: #0278f6;
    box-shadow: none;
  }

  .btn-primary:hover,
  .btn-primary:focus {
    background-color: #025bb5;
    border-color: #025bb5;
    box-shadow: none;
  }

  .btn-outline-primary {
    color: #0278f6;
    border-color: #0278f6;
    box-shadow: none;
  }

  .btn-outline-primary:hover {
    background-color: #0278f6;
    color: #fff;
    border-color: #0278f6;
    box-shadow: none;
  }

  .forgot-password {
    color: #fc8c04;
    font-weight: 500;
  }

  .forgot-password:hover {
    color: #e67e02;
    text-decoration: underline;
  }

  .card {
    padding: 0 80px;
    margin-right: 120px;
    box-shadow: none;
  }

  footer {
    background-color:#fcfceb ;
  }

  .input-group {
    border-radius: 0.8rem;
    border: 0.5px solid black;
  }

  .card {
    margin-right: 15vw;
  }

</style>

</head>
<body>
<div class="container-fluid vh-110 d-flex align-items-center justify-content-center py-3"
     style="background-color: #fcfceb;">
  <div class="row w-100">
    <div class="col-lg-7 d-flex flex-column align-items-center justify-content-center text-center">
      <h1 class="display-6 fw-bold" style="font-family: DM Serif Display, serif;">
        Assuri – Ensemble,<br> assurons votre avenir.
      </h1>
      <img src="${url.resourcesPath}/images/login%20illustration.png" alt="Illustration" class="img-fluid mb-3"
           style="width: 33vw; height: fit-content;">
      <p class="fs-4" style="font-family: DM Serif Display, serif;
                ">
        Gestion sécurisée, simple et rapide.<br>Accédez à vos contrats et services à distance.
      </p>
      <p class="fw-bold text-secondary d-flex align-items-center gap-2">
        <i class="bi bi-telephone-fill text-primary"></i>Contactez-nous à 75 777 777
      </p>
    </div>

    <div class="col-lg-5">
      <div class="card border-0"
           style="width:32vw; background-color: rgba(255, 255, 255, 0.9);">
        <div class="text-center">
          <img src="${url.resourcesPath}/images/logo.png" alt="Logo" class="img-fluid my-0"
               style="width: 12vw; height: 150px;">
        </div>

<form onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
          <div class="input-group mb-3 border-1 border-secondary">
            <span class="input-group-text bg-transparent border-0 fs-4 ps-3 pe-0"><i class="bi bi-person bg-transparent text-center"></i></span>
            <div class="form-floating">
              <input type="text" class="form-control bg-transparent border-0 shadow-none" name="username" id="identifiant" placeholder="Identifiant" aria-invalid="false" autofocus>
              <label class="" for="identifiant">Identifiant</label>
            </div>
          </div>

          <div class="input-group mb-3 border-1 border-secondary">
            <span class="input-group-text bg-transparent border-0 fs-4 ps-3 pe-0"><i class="bi bi-lock bg-transparent text-center"></i></span>
            <div class="form-floating">
              <input type="password" class="form-control bg-transparent border-0 shadow-none" name="password"
                     id="password" placeholder="Mot de passe">
              <label class="" for="password">Mot de passe</label>
            </div>
            <span class="input-group-text bg-transparent border-0 pe-3 fs-6">
                    <i class="bi bi-eye-slash" id="togglePassword"></i>
                </span>
          </div>


          <div class="form-check mb-3">
            <input type="checkbox" class="form-check-input border-secondary" id="remember_me"
                   name="rememberMe" checked>
            <label class="form-check-label" name="rememberMe" for="remember_me">Se souvenir de moi</label>
          </div>

          <div class="d-grid text-center mb-3 mt-5">
            <input type="hidden" id="id-hidden-input" name="credentialId" />
            <input type="submit" id="kc-login" class="btn btn-primary btn-lg rounded-pill border-1 border-secondary" value="Se connecter" name="login">
          </div>
  <script type="module" src="/resources/rk0vz/login/base/js/passwordVisibility.js"></script>


  <div class="text-center mb-4">
            <a href="${url.loginResetCredentialsUrl}" class="text-decoration-none forgot-password">Mot de passe oublié ?</a>
          </div>
          <hr class="border-1 border-black">
          <div class="text-center mb-4 mt-5 fs-6">
            <p>Vous n'avez pas de compte ?</p>
          </div>
          <div class="d-grid mb-5">
            <button type="button"  name="register" class="btn btn-outline-primary btn-lg rounded-pill"><a href="${url.registrationUrl}" >Créer un compte</a></button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<footer class="text-center py-3 border-1 text-secondary border-top">
  <p>&copy; 2025 Assuri. Tous droits réservés.</p>
</footer>

<!-- Bootstrap JS and Popper.js -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"></script>
<script>
  // document.getElementById('togglePassword').addEventListener('click', function () {
  //   const passwordInput = document.querySelector('#floatingPassword');
  //
  //   if (passwordInput.type === 'password') {
  //     passwordInput.type = 'text';
  //     this.classList.remove('bi-eye-slash');
  //     this.classList.add('bi-eye');
  //   } else {
  //     passwordInput.type = 'password';
  //     this.classList.remove('bi-eye');
  //     this.classList.add('bi-eye-slash');
  //   }
  // });



</script>
</body>


<template>
  <div class="view-container">
    <div class="account-wrapper">
      <h1>Mein Account</h1>
      <div class="profile-settings">
        <div class="username">
          <p>Benutzername</p>
          <p>Lukas Schodl</p>
        </div>
        <div class="username">
          <p>E-Mail-Adresse</p>
          <p>lukas.schodl@htl.rennweg.at</p>
        </div>
      </div>
      <div class="account-actions">
        <router-link class="router-link" to="passwort-ändern">Passwort ändern</router-link>
        <button @click="logOutOverlayVisible = true">Abmelden</button>
        <button @click="deleteAccountOverlayVisible = true">Account löschen</button>
      </div>
    </div>
    <Overlay :is-visible="logOutOverlayVisible" @close-overlay="logOutOverlayVisible=false">test</Overlay>
    <Overlay :is-visible="deleteAccountOverlayVisible" @close-overlay="deleteAccountOverlayVisible=false">based</Overlay>
    <NavBar :active-icon="1"></NavBar>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import Overlay from "@/components/Overlay.vue";

export default {
  name: "AccountView",
  components: {NavBar, Overlay},
  data() {
    return {
      logOutOverlayVisible: false,
      deleteAccountOverlayVisible: false
    }
  }
}
</script>

<style scoped lang="scss">
@import "../assets/colors";

.view-container {
  min-height: 100vh;
  background-image: url("../assets/background.png");
  background-position: center;

  .account-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;

    .account-actions {
      width: 60%;

      button, .router-link {
        display: block;
        width: 100%;
        height: 3rem;
        border: 2px solid transparent;
        color: white;
        margin-top: 2.5rem;
        font-weight: bolder;
        background: $blue;
        border-radius: 10px;
        padding-left: 0.5rem;
      }
    }
  }

}
</style>
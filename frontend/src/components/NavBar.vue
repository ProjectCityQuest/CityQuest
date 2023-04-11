<template>
  <div class="nav-bar-wrapper">
    <router-link to="/account" class="nav-item" :class="activeIcon === 1 ? 'active' : 'inactive'">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" class="account-icon">
          <circle cx="25" cy="25" r="25" style="fill:#fff" class="background-circle"/>
          <path d="M25,25.59c-7.09,0-12.88,6.44-13.21,14.52H38.21C37.88,32,32.09,25.59,25,25.59Z" transform="translate(0)"/>
          <circle cx="25.7" cy="16.56" r="6.45"/>
        </svg>
    </router-link>
    <router-link to="/karte" class="nav-item" :class="activeIcon === 2 ? 'active' : 'inactive'">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" class="map-icon">
        <circle cx="25" cy="25" r="25" style="fill:#fff" class="background-circle"/>
        <path
            d="M25,10c-3.85,0-10.27,2.25-10.27,9.3C14.73,28.77,25,40,25,40S35.27,28.77,35.27,19.3C35.27,12.25,28.85,10,25,10Zm0,13.48a4,4,0,1,1,4-4A4,4,0,0,1,25,23.48Z"
            transform="translate(0)"/>
      </svg>
    </router-link>
    <router-link to="/puzzle" class="nav-item" :class="activeIcon === 3 ? 'active' : 'inactive'">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" class="puzzle-icon">
        <circle cx="25" cy="25" r="25" style="fill:#fff" class="background-circle"/>
        <path
            d="M40.33,22.74c-1.07,0-1.75,1-2.67.84a.88.88,0,0,1-.8-.84v-7a.88.88,0,0,0-.89-.88H27.81a1,1,0,0,1-.93-.85c-.15-1,.93-1.69.93-2.82S27,8.4,24.22,8.4,19.9,10,20.89,12.63c.8,2.12-.26,2.26-.26,2.26h-7.3a.87.87,0,0,0-.88.88v7.14s.21,1.3,2,.28c1.27-.72,3.78.56,3.78,3.5s-1.93,3.77-2.52,3.77c-1,0-1.66-1.14-2.53-1a1,1,0,0,0-.75,1v7.62a.88.88,0,0,0,.88.88H21a1,1,0,0,0,.93-.8c.15-.93-.93-1.61-.93-2.68,0-.62.8-2.67,3.59-2.67s4,2.67,3.31,4c-1,1.91.27,2.14.27,2.14H36a.89.89,0,0,0,.89-.88V29.19S37,28.24,39,29c2.5.9,4-.47,4-3S41.4,22.74,40.33,22.74Z"
            transform="translate(0)"/>
      </svg>
    </router-link>
    <router-link to="/sammelbuch" class="nav-item" :class="activeIcon === 4 ? 'active' : 'inactive'">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" class="collection-icon">
        <circle cx="25" cy="25" r="25" style="fill:#fff" class="background-circle"/>
        <path
            d="M35.21,11.16H14.51a.78.78,0,0,0-.78.78V38.51a.78.78,0,0,0,.78.79h20.7a.79.79,0,0,0,.79-.79V11.94A.78.78,0,0,0,35.21,11.16ZM31.67,21.28,29.06,18a.59.59,0,0,0-.94,0l-2.62,3.3V13.87h6.17Z"/>
      </svg>
    </router-link>
  </div>
</template>

<script>
export default {
  name: "NavBar",
  props: {
    activeIcon: Number
  },
}
</script>

<style scoped lang="scss">
@import "src/assets/colors.scss";

.nav-bar-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 70px;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);

  .nav-item {
    height: 50px;

    svg {
      height: 50px;
    }

    &.inactive {
      svg {
        fill: white;

        .background-circle {
          visibility: hidden;
        }
      }
    }

    .account-icon {
      fill: $dark_red;
    }

    .map-icon {
      fill: $orange;
    }

    .puzzle-icon {
      fill: $dark_green;
    }

    .collection-icon {
      fill: $blue;
    }
  }
}
</style>
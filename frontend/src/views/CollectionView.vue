<template>
  <div class="header">
    <div class="heading" @click="this.sortingOverlay=false">
      <h1>Sammelbuch</h1>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" class="collection-icon">
        <path
            d="M35.21,11.16H14.51a.78.78,0,0,0-.78.78V38.51a.78.78,0,0,0,.78.79h20.7a.79.79,0,0,0,.79-.79V11.94A.78.78,0,0,0,35.21,11.16ZM31.67,21.28,29.06,18a.59.59,0,0,0-.94,0l-2.62,3.3V13.87h6.17Z"/>
      </svg>
    </div>
    <div class="filter">
      <input type="text" id="searchText" v-model="inputText" name="searchText" placeholder="Suchtext eingeben"
             @click="this.sortingOverlay=false">
      <div @click="this.sortingOverlay = !this.sortingOverlay">
        <svg xmlns="http://www.w3.org/2000/svg" height="48" viewBox="0 96 960 960" width="48">
          <path
              d="M322 606V290L202 410l-42-42 193-193 193 193-42 42-122-121v317h-60Zm285 369L414 781l42-41 120 120V544h60v317l122-121 42 42-193 193Z"/>
        </svg>
      </div>
      <CQButton @click="filter()" b-style="login" status="active">Suchen</CQButton>
    </div>
    <div class="hr"></div>
  </div>
  <div v-if="this.sortingOverlay" class="sortingOverlay">
    <p class="sort">sortieren nach</p>
    <div class="options">
      <p @click="sortEntries(1)">neueste Einträge zuerst</p>
      <p @click="sortEntries(2)">älteste Einträge zuerst</p>
      <p @click="sortEntries(3)">Spot-Name absteigend (A-Z)</p>
      <p @click="sortEntries(4)">Spot-Name aufsteigend (Z-A)</p>
    </div>
  </div>
  <div :class="this.loading?'entry-container loading':'entry-container'" @click="this.sortingOverlay=false">
    <CollectionEntry v-for="entry in filteredEntries" :id="entry.id" :location="entry.location" :date="entry.timestamp"
                     :text="entry.text"
                     :image="entry.image">
    </CollectionEntry>
    <div v-if="loading" class="loading"></div>
    <div v-else-if="listEmpty" class="empty">Dein Suchtext wurde in keinem deiner Einträge gefunden</div>
    <div v-else-if="filteredEntries.length===0" class="empty">Du hast noch keine Einträge</div>
  </div>
  <NavBar :active-icon="4"></NavBar>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import CollectionEntry from "@/components/CollectionEntry.vue";
import CQButton from "@/components/CQButton.vue";

export default {
  name: "CollectionView",
  components: {
    NavBar,
    CollectionEntry,
    CQButton
  },
  data() {
    return {
      sortingOverlay: false,
      inputText: "",
      filterText: "",
      listEmpty: false,
      sortType: 0,
      entries: [],
      loading: true,
    }
  },
  async mounted() {
    await this.fetchData().then(data => {
      this.loading = false
      this.entries = data.entries
    })
  },
  computed: {
    filteredEntries() {
      let list = this.getTextFilter()
      switch (this.sortType) {
        case 0:
          return list
        case 1:
          return list.sort((x, y) => Date.parse(y.timestamp) - Date.parse(x.timestamp))
        case 2:
          return list.sort((x, y) => Date.parse(x.timestamp) - Date.parse(y.timestamp))
        case 3:
          return list.sort((x, y) => (x.location > y.location) ? 1 : -1)
        case 4:
          return list.sort((x, y) => (y.location > x.location) ? 1 : -1)
      }
    }
  },
  methods: {
    getTextFilter(){
      if (this.filterText !== "") {
        let filtered = this.entries.filter(entry => entry.text.toUpperCase().includes(this.filterText) || entry.location.toUpperCase().includes(this.filterText))
        this.listEmpty = filtered.length === 0
        return filtered
      } else {
        this.listEmpty = this.entries.length === 0
        return this.entries
      }
    },
    filter() {
      this.filterText = this.inputText.toUpperCase()
      this.sortingOverlay = false
    },
    sortEntries(sortType) {
      this.sortType = sortType
      this.sortingOverlay = false
    },
    getCookie(cname) {
      let name = cname + "=";
      let decodedCookie = decodeURIComponent(document.cookie);
      let ca = decodedCookie.split(';');
      for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
          return c.substring(name.length, c.length);
        }
      }
      return false;
    },
    fetchData() {
      return fetch(`http://${window.location.hostname}:8080/api/getcollection`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'sessionKey': this.getCookie('sessionKey')
        },
        withCredentials: true,
        credentials: 'same-origin'
      }).then(response => response.json())
    }
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";

.header {
  background-color: $white;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;

  .heading {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;

    height: 100px;

    h1 {
      font-size: 32px;
    }

    svg {
      height: 45px;
      fill: $orange;
    }
  }

  .filter {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin: 0 auto 0 auto;
    width: 90%;

    * {
      height: 2rem;
    }

    input {
      border: solid #757575 2px;
      border-radius: 5px;
      font-size: 0.9rem;
      padding-left: 5px;
      width: 100%;
    }

    button {
      border-radius: 5px;
      text-align: center;
      max-width: 110px;
    }
  }

  .hr {
    width: 100%;
    height: 2px;
    background-color: $light_gray;
    margin-top: 15px;
  }

}

.sortingOverlay {
  position: fixed;
  z-index: 4;
  left: 5%;
  top: calc(117px + 2rem + 2px);
  background-color: $white;
  border-radius: 0 0 10px 10px;
  border: solid #d9d9d9 2px;

  .sort {
    color: #838383;
    font-size: 0.9rem;
    padding: 10px 10px 0 10px;
  }

  .options {
    * {
      height: 2.25rem;
      display: flex;
      align-items: center;
      padding: 0 10px 0 10px;

      &:hover {
        background-color: #eaeaea;
      }
    }
  }
}

.entry-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: calc(117px + 2rem);
  padding-bottom: 85px;
  width: 100%;
  height: calc(100vh - 100px - 2rem);
  overflow: scroll;
  background-image: url("src/assets/background.png");
  background-repeat: no-repeat;
  background-attachment: fixed;


  * {
    width: 90%;
    margin-top: 15px;
  }

  &.loading{
    padding-bottom: 0;
    justify-content: center;
  }

  .loading{
    border: 1rem solid $light_gray;
    border-radius: 50%;
    border-top-color: $blue;
    width: 100px;
    height: 100px;
    margin-top: -70px;

    -webkit-animation: spin 2s linear infinite;
    animation: spin 2s linear infinite;
  }

  .empty {
    text-align: center;
    color: $gray;
    margin: auto 0 auto 0;
  }
}

@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
<template>
  <div class="entry">
    <div class="top">
      <h3>{{ this.location }}</h3>
      <p>{{ this.date }}</p>
    </div>
    <div class="hr"></div>
    <div class="bottom">
      <p>{{ this.text }}</p>
      <img v-if="this.image!=null" :src="image">
    </div>
  </div>
</template>

<script>
export default {
  name: "CollectionEntry",
  props:{
    id: Number,
    location: String,
    date: String,
    text: String,
    image: String
  }
}
</script>

<style scoped lang="scss">
@import "src/assets/colors";
.entry{
  width:100%;
  background-color: #eaeaea;
  border-radius: 10px;

  .top{
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    padding: 10px;
    gap: 10px;

    h3{
      font-size: 1.25rem;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
    }

    p{
      display: flex;
      align-items: flex-end;
    }
  }

  .hr{
    width: 100%;
    height: 2px;
    background-color: #c9c9c9;
  }

  .bottom{
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    padding: 10px;
    height: 110px;
    gap: 10px;

    overflow: hidden;

    p{
      height: 110px;
      line-height: 19px;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 6;
      text-overflow: ellipsis;
      overflow: hidden;
      font-size: 0.95rem;
    }


    img{
      width: 110px;
      height:110px;
      border-radius: 5px;
      object-fit: cover;
    }
  }
}
</style>
<template>
  <div class="star-container">
    <div class="star">
      <div v-if="this.currentRating>0" class="star-filled" @click="this.currentRating>1?setRating(1):setRating(0)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path d="m233 976 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Z"/>
        </svg>
      </div>
      <div v-else class="star-empty" @click="setRating(1)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path
              d="m323 851 157-94 157 95-42-178 138-120-182-16-71-168-71 167-182 16 138 120-42 178Zm-90 125 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Zm247-355Z"/>
        </svg>
      </div>
    </div>
    <div class="star">
      <div v-if="this.currentRating>1" class="star-filled" @click="this.currentRating>2?setRating(2):setRating(1)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path d="m233 976 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Z"/>
        </svg>
      </div>
      <div v-else class="star-empty" @click="setRating(2)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path
              d="m323 851 157-94 157 95-42-178 138-120-182-16-71-168-71 167-182 16 138 120-42 178Zm-90 125 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Zm247-355Z"/>
        </svg>
      </div>
    </div>
    <div class="star">
      <div v-if="this.currentRating>2" class="star-filled" @click="this.currentRating>3?setRating(3):setRating(2)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path d="m233 976 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Z"/>
        </svg>
      </div>
      <div v-else class="star-empty" @click="setRating(3)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path
              d="m323 851 157-94 157 95-42-178 138-120-182-16-71-168-71 167-182 16 138 120-42 178Zm-90 125 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Zm247-355Z"/>
        </svg>
      </div>
    </div>
    <div class="star">
      <div v-if="this.currentRating>3" class="star-filled" @click="this.currentRating>4?setRating(4):setRating(3)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path d="m233 976 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Z"/>
        </svg>
      </div>
      <div v-else class="star-empty" @click="setRating(4)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path
              d="m323 851 157-94 157 95-42-178 138-120-182-16-71-168-71 167-182 16 138 120-42 178Zm-90 125 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Zm247-355Z"/>
        </svg>
      </div>
    </div>
    <div class="star">
      <div v-if="this.currentRating>4" class="star-filled" @click="this.currentRating>5?setRating(5):setRating(4)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path d="m233 976 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Z"/>
        </svg>
      </div>
      <div v-else class="star-empty" @click="setRating(5)">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 96 960 960">
          <path
              d="m323 851 157-94 157 95-42-178 138-120-182-16-71-168-71 167-182 16 138 120-42 178Zm-90 125 65-281L80 506l288-25 112-265 112 265 288 25-218 189 65 281-247-149-247 149Zm247-355Z"/>
        </svg>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Rating.vue",
  data() {
    return {
      currentRating: 0
    }
  },
  methods: {
    setRating(rating) {
      this.currentRating = rating
      this.$emit('changedRating', this.currentRating)
    }
  }
}
</script>

<style scoped lang="scss">
@import "../assets/colors";

.star-container{
  display: flex;
  flex-direction: row;
  gap:1px;

  svg{
    height: 2rem;
    width: 2rem;
  }

  .star-filled svg{
    fill: $yellow;
  }
  .star-empty svg{
    fill: $gray;
  }
}
</style>
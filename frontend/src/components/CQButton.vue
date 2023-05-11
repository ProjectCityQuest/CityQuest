<template>
  <button :class="bStyle + ' ' + status" :disabled="status === 'inactive' ||status === 'waiting'">
    <slot></slot>
  </button>
</template>

<script>
export default {
  name: "CQButton",
  props: {
    status: String,
    bStyle: String
  }
}
</script>

<style scoped lang="scss">
@import "../assets/colors";

button {
  font-family: 'Berlin Sans FB', sans-serif;
  width: 100%;
  height: 3rem;
  padding: 0.5rem;
  border: 2px solid transparent;
  border-radius: 10px;

  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.login, .register {
  &.active {
    color: white;
    background: $blue;
  }

  &.inactive {
    color: white;
    background: $gray;
  }

  &.waiting {
    color: $blue;
    border-color: $blue;
    background: transparent;
  }
}

.delete {
  &.default {
    color: white;
    background: $red;
  }

  &:hover{
    background: $yellow;
  }

  &.pending {

  }
}

.puzzle-piece {
  width: 3rem;
  display: flex;
  justify-content: center;
  align-items: center;

  &.active {
    color: white;
    background: $middle_green;
  }

  &.inactive {
    color: white;
    background: $gray;
  }
}

.collection-entry {
  background: $orange;
  color: white;

  &.in-range {

  }
}
</style>
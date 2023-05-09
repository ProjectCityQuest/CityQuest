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
.orange {
  &.ready {
    color: white;
    background: $orange;
  }

  &.waiting {
    color: $orange;
    border-color: $orange;
    background: transparent;
  }
}

.delete {
  &.default {
    color: $red;
    border: solid 2px $red;
    background: transparent;
  }

  &:hover{
    background: $yellow;
  }

  &.pending {
    color: white;
    background: $red;
  }
}
</style>
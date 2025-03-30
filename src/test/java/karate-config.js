function fn() {
  karate.log('setting env to h2');
  karate.env = 'h2'; // this sets the Spring Boot profile
  return {};
}
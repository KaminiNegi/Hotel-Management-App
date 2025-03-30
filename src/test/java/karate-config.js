karate.set('polyglot.js.nashorn-compat', true); // ✅ Quick fix for Java 21+

function fn() {
  karate.log('setting env to h2');
  karate.env = 'h2'; // this sets the Spring Boot profile
  return {};
}
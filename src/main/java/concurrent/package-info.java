/**
 * 包注解
 * <p>
 * 包注释
 */
@Deprecated
package concurrent;

//包内类
class MyClass {
    public static void main(String[] args) {
        //获得包对象
        Package pack = Package.getPackage("concurrent");
        String specificationTitle = pack.getSpecificationTitle();
        System.out.println(specificationTitle);
    }

}
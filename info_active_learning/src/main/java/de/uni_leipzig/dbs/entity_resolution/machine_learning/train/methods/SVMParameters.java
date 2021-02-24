package de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods;

/**
 * Created by christen on 08.08.2017.
 */
public class SVMParameters {

  double c;

  double gamma;

  double eps;

  public SVMParameters(double c, double gamma, double epsilon) {
    this.c = c;
    this.gamma = gamma;
    this.eps = epsilon;
  }

  public double getC() {
    return c;
  }

  public double getGamma() {
    return gamma;
  }

  public double getEps() {
    return eps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SVMParameters that = (SVMParameters) o;

    if (Double.compare(that.c, c) != 0) return false;
    if (Double.compare(that.gamma, gamma) != 0) return false;
    return Double.compare(that.eps, eps) == 0;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(c);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(gamma);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(eps);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "SVMParameters{" +
            "c=" + c +
            ", gamma=" + gamma +
            ", eps=" + eps +
            '}';
  }
}

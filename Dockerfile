FROM ghcr.io/graalvm/graalvm-ce:ol8-java17

ADD build/distributions/inspections-0.0.1.tar /
WORKDIR /inspections-0.0.1

CMD ["bin/inspections"]

package com.personal.movie.domain.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Genre {
    ACTION(28, "액션"),
    ADVENTURE(12, "모험"),
    ANIMATION(16, "애니메이션"),
    COMEDY(35, "코미디"),
    CRIME(80, "범죄"),
    DOCUMENTARY(99, "다큐멘터리"),
    DRAMA(18, "드라마"),
    FAMILY(10751, "가족"),
    FANTASY(14, "판타지"),
    HISTORY(36, "역사"),
    HORROR(27, "공포"),
    MUSIC(10402, "음악"),
    MYSTERY(9648, "미스터리"),
    ROMANCE(10749, "로맨스"),
    SF(878, "SF"),
    TV_MOVIE(10770, "TV 영화"),
    THRILLER(53, "스릴러"),
    WAR(10752, "전쟁"),
    WESTERN(37, "서부");

    private static final Map<Integer, String> ID_TO_NAME_MAP = Arrays.stream(values())
        .collect(Collectors.toMap(Genre::getId, Genre::getName));

    private final int id;
    private final String name;

    // API 를 통해 영화 데이터를 받아올 때, 장르가 Integer 타입인 id로 주어지므로 한글로 바꿔서 저장
    public static String nameOfId(int id) {
        return ID_TO_NAME_MAP.get(id);
    }

}

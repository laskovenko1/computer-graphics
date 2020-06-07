in vec2 TexCoord;

out vec4 color;

uniform sampler2D u_texture;

void main()
{
    color = texture(u_texture, TexCoord);
}